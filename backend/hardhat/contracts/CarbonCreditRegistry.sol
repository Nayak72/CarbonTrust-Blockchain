// SPDX-License-Identifier: MIT
pragma solidity ^0.8.24;

/**
 * @title CarbonCreditRegistry v2
 * @notice On-chain registry for quality-adjusted carbon credits.
 *         Each record encodes the full analytical model output:
 *         Tx = (company_id, period_id, Credits_adj, E_total, E_red, report_hash, timestamp)
 */
contract CarbonCreditRegistry {

    // ============================================================
    // STRUCTS & EVENTS
    // ============================================================

    struct CreditRecord {
        string  facilityId;         // company_id in the analytical model (Supabase UUID)
        string  periodId;           // Human-readable period, e.g. "2025-05"
        uint256 creditsAdj;         // Credits_adj × 1e6 (quality-adjusted, stored in grams)
        uint256 totalEmissions;     // E_total × 1e6 (grams)
        uint256 emissionReduction;  // E_red × 1e6 (grams)
        bytes32 reportHash;         // SHA-256 of the full emission report JSON
        uint256 timestamp;          // block.timestamp at issuance
        string  ipfsCid;            // IPFS CID for fetching the full report
        address issuedBy;           // Backend wallet address that called issueCredit()
    }

    event CreditIssued(
        bytes32 indexed creditId,
        string  facilityId,
        string  periodId,
        uint256 creditsAdj,
        uint256 totalEmissions,
        uint256 emissionReduction,
        bytes32 reportHash,
        uint256 timestamp,
        string  ipfsCid,
        address issuedBy
    );

    // ============================================================
    // STATE VARIABLES
    // ============================================================

    address public owner;
    mapping(bytes32 => CreditRecord) public credits;
    bytes32[] public creditIds;

    // ============================================================
    // MODIFIERS
    // ============================================================

    modifier onlyOwner() {
        require(msg.sender == owner, "Not authorised");
        _;
    }

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    constructor() {
        owner = msg.sender;
    }

    // ============================================================
    // CORE FUNCTIONS
    // ============================================================

    /**
     * @dev Issue a carbon credit record on-chain.
     *
     * @param facilityId        UUID of the facility (Supabase)
     * @param periodId          Human-readable period string, e.g. "2025-05"
     * @param creditsAdj        Quality-adjusted credits in grams (1 tonne = 1_000_000)
     * @param totalEmissions    Total CO₂ emissions in grams
     * @param emissionReduction Emission reduction vs baseline in grams
     * @param reportHash        SHA-256 of the canonical emission report JSON (bytes32)
     * @param ipfsCid           IPFS CID of the full emission report
     * @return creditId         Unique on-chain ID for this credit record
     *
     * creditId is derived from (facilityId, periodId_hourly, reportHash) so it is
     * deterministic per reporting period and unique across reruns (reportHash differs).
     * The "hourly" period used for the keccak is derived server-side and encoded
     * into reportHash, so no additional parameter is needed here.
     */
    function issueCredit(
        string  calldata facilityId,
        string  calldata periodId,
        uint256 creditsAdj,
        uint256 totalEmissions,
        uint256 emissionReduction,
        bytes32 reportHash,
        string  calldata ipfsCid
    ) external onlyOwner returns (bytes32 creditId) {

        // Deterministic ID: (facilityId, periodId, reportHash)
        creditId = keccak256(
            abi.encodePacked(facilityId, periodId, reportHash)
        );

        require(credits[creditId].timestamp == 0, "Duplicate credit record");

        credits[creditId] = CreditRecord({
            facilityId:        facilityId,
            periodId:          periodId,
            creditsAdj:        creditsAdj,
            totalEmissions:    totalEmissions,
            emissionReduction: emissionReduction,
            reportHash:        reportHash,
            timestamp:         block.timestamp,
            ipfsCid:           ipfsCid,
            issuedBy:          msg.sender
        });

        creditIds.push(creditId);

        emit CreditIssued(
            creditId,
            facilityId,
            periodId,
            creditsAdj,
            totalEmissions,
            emissionReduction,
            reportHash,
            block.timestamp,
            ipfsCid,
            msg.sender
        );

        return creditId;
    }

    /**
     * @dev Get a credit record by its ID.
     */
    function getCredit(bytes32 creditId)
        external view returns (CreditRecord memory)
    {
        require(credits[creditId].timestamp != 0, "Credit not found");
        return credits[creditId];
    }

    /**
     * @dev Get total number of credits issued.
     */
    function getTotalCreditsCount() external view returns (uint256) {
        return creditIds.length;
    }

    /**
     * @dev Get a paginated slice of credit IDs.
     */
    function getCreditIds(uint256 offset, uint256 limit)
        external view returns (bytes32[] memory)
    {
        uint256 end = offset + limit;
        if (end > creditIds.length) end = creditIds.length;
        bytes32[] memory result = new bytes32[](end - offset);
        for (uint256 i = offset; i < end; i++) {
            result[i - offset] = creditIds[i];
        }
        return result;
    }

    /**
     * @dev Transfer ownership (for key rotation).
     */
    function transferOwnership(address newOwner) external onlyOwner {
        require(newOwner != address(0), "Invalid address");
        owner = newOwner;
    }
}
