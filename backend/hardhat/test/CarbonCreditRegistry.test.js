const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("CarbonCreditRegistry", function () {
  let registry;
  let owner;
  let otherAccount;

  beforeEach(async function () {
    [owner, otherAccount] = await ethers.getSigners();
    const CarbonCreditRegistry = await ethers.getContractFactory("CarbonCreditRegistry");
    registry = await CarbonCreditRegistry.deploy();
    await registry.waitForDeployment();
  });

  describe("Deployment", function () {
    it("Should set the deployer as the owner", async function () {
      expect(await registry.owner()).to.equal(owner.address);
    });

    it("Should start with zero credits", async function () {
      expect(await registry.getTotalCreditsCount()).to.equal(0);
    });
  });

  describe("Credit Issuance", function () {
    it("Should issue a credit and emit an event", async function () {
      const facilityId = "550e8400-e29b-41d4-a716-446655440000";
      const creditsIssued = 1000000; // 1 tonne in grams
      const ipfsCid = "bafybeigdyrzt5sfp7udm7hu76uh7y26nf3efuylqabf3oclgtqy55fbzdi";

      const tx = await registry.issueCredit(facilityId, creditsIssued, ipfsCid);
      const receipt = await tx.wait();

      // Check event was emitted
      const event = receipt.logs[0];
      expect(event).to.not.be.undefined;

      // Check credit count
      expect(await registry.getTotalCreditsCount()).to.equal(1);
    });

    it("Should reject non-owner credit issuance", async function () {
      await expect(
        registry.connect(otherAccount).issueCredit(
          "facility-123",
          1000000,
          "bafytest"
        )
      ).to.be.revertedWith("Not authorised");
    });

    it("Should retrieve a credit by ID", async function () {
      const facilityId = "facility-test-001";
      const creditsIssued = 500000;
      const ipfsCid = "bafytest123";

      const tx = await registry.issueCredit(facilityId, creditsIssued, ipfsCid);
      await tx.wait();

      // Get credit ID from creditIds array
      const creditId = await registry.creditIds(0);
      const credit = await registry.getCredit(creditId);

      expect(credit.facilityId).to.equal(facilityId);
      expect(credit.creditsIssued).to.equal(creditsIssued);
      expect(credit.ipfsCid).to.equal(ipfsCid);
      expect(credit.issuedBy).to.equal(owner.address);
    });
  });

  describe("Pagination", function () {
    it("Should return paginated credit IDs", async function () {
      // Issue 5 credits
      for (let i = 0; i < 5; i++) {
        await registry.issueCredit(`facility-${i}`, (i + 1) * 100000, `cid-${i}`);
      }

      expect(await registry.getTotalCreditsCount()).to.equal(5);

      // Get first 3
      const page1 = await registry.getCreditIds(0, 3);
      expect(page1.length).to.equal(3);

      // Get next 3 (should only return 2)
      const page2 = await registry.getCreditIds(3, 3);
      expect(page2.length).to.equal(2);
    });
  });

  describe("Ownership", function () {
    it("Should transfer ownership", async function () {
      await registry.transferOwnership(otherAccount.address);
      expect(await registry.owner()).to.equal(otherAccount.address);
    });

    it("Should reject transfer to zero address", async function () {
      await expect(
        registry.transferOwnership(ethers.ZeroAddress)
      ).to.be.revertedWith("Invalid address");
    });

    it("Should reject non-owner transfer", async function () {
      await expect(
        registry.connect(otherAccount).transferOwnership(otherAccount.address)
      ).to.be.revertedWith("Not authorised");
    });
  });
});
