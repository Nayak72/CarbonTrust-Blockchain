// SPDX-License-Identifier: MIT
pragma solidity ^0.8.24;

contract CarbonCreditRegistry {

    // ============================================================
    // STRUCTS & EVENTS
    // ============================================================

    struct CreditRecord {
        string facilityId;       // UUID of the facility from Supabase
        uint256 creditsIssued;   // Credits in kg (multiply by 1000 to avoid decimals)
        uint256 timestamp;       // Unix timestamp of issuance
        string ipfsCid;          // IPFS CID of the emission report
        address issuedBy;        // Address that called this function (backend wallet)
    }

    event CreditIssued(
        bytes32 indexed creditId,
        string facilityId,
        uint256 creditsIssued,
        uint256 timestamp,
        string ipfsCid,
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
     * @dev Issue a carbon credit record on-chain
     * @param facilityId UUID of the facility (from Supabase)
     * @param creditsIssued Number of credits in grams (e.g. 1 tonne = 1000000)
     * @param ipfsCid IPFS CID of the emission report
     * @return creditId Unique ID of this credit record on-chain
     */
    function issueCredit(
        string calldata facilityId,
        uint256 creditsIssued,
        string calldata ipfsCid
    ) external onlyOwner returns (bytes32 creditId) {

        creditId = keccak256(
            abi.encodePacked(facilityId, creditsIssued, block.timestamp, ipfsCid)
        );

        require(credits[creditId].timestamp == 0, "Duplicate credit record");

        credits[creditId] = CreditRecord({
            facilityId: facilityId,
            creditsIssued: creditsIssued,
            timestamp: block.timestamp,
            ipfsCid: ipfsCid,
            issuedBy: msg.sender
        });

        creditIds.push(creditId);

        emit CreditIssued(
            creditId,
            facilityId,
            creditsIssued,
            block.timestamp,
            ipfsCid,
            msg.sender
        );

        return creditId;
    }

    /**
     * @dev Get a credit record by its ID
     */
    function getCredit(bytes32 creditId)
        external view returns (CreditRecord memory) {
        require(credits[creditId].timestamp != 0, "Credit not found");
        return credits[creditId];
    }

    /**
     * @dev Get total number of credits issued
     */
    function getTotalCreditsCount() external view returns (uint256) {
        return creditIds.length;
    }

    /**
     * @dev Get all credit IDs (paginated)
     */
    function getCreditIds(uint256 offset, uint256 limit)
        external view returns (bytes32[] memory) {
        uint256 end = offset + limit;
        if (end > creditIds.length) end = creditIds.length;
        bytes32[] memory result = new bytes32[](end - offset);
        for (uint256 i = offset; i < end; i++) {
            result[i - offset] = creditIds[i];
        }
        return result;
    }

    /**
     * @dev Transfer ownership (for key rotation)
     */
    function transferOwnership(address newOwner) external onlyOwner {
        require(newOwner != address(0), "Invalid address");
        owner = newOwner;
    }
}
