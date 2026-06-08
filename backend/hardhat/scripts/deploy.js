const { ethers } = require("hardhat");

async function main() {
  const [deployer] = await ethers.getSigners();
  console.log("Deploying with account:", deployer.address);
  console.log("Account balance:", (await deployer.provider.getBalance(deployer.address)).toString());

  const CarbonCreditRegistry = await ethers.getContractFactory("CarbonCreditRegistry");
  const contract = await CarbonCreditRegistry.deploy();
  await contract.waitForDeployment();

  const address = await contract.getAddress();
  console.log("CarbonCreditRegistry deployed to:", address);
  console.log("Add this to your .env: CONTRACT_ADDRESS=" + address);
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
