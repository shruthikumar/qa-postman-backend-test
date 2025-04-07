# qa-enterprise-sdk-backend-test automated-test-suite

sdk test automation suite

## Maintainers

- [enterprise-qa](https://github.com/orgs/truecaller/teams/enterprise-qa)

_**Description**_ :
This project is a Java-based API automation framework using REST Assured, built for testing RESTful web services efficiently. It follows best practices in API test automation, supports test reporting, and it is integrated with Github Actions

🚀 Features
1. [ ] REST API Testing using REST Assured
2. [ ] Test orchestration using TestNG
3. [ ] Test execution with Maven
5. [ ] Assertions using Hamcrest and TestNg
7. [ ] JSON Schema validation
9. [ ] Centralized configuration
11. [ ] Supports multiple environments
13. [ ] Integrated reporting (e.g., Allure used and Extent Reports supported)
15. [ ] CI/CD-ready with github-Actions with Slack Notification for Work flow run status


🧰 Tech Stack

| Tool/Library | Purpose                              |
|---------|--------------------------------------|
| Java    | Programming language                 |
| REST Assured | API test library                     |
| TestNG  | Test framework / Runner / Assertions |
| Maven   | Build & dependency management        |
| Allure / Extent | Test reporting                       |
| Jackson / Gson | JSON parsing                         |




**⚙️ Installation & Setup**

**Prerequisites**
1. [ ] Java 17
2. [ ] Maven
3. [ ] Allure to view Reports locally
4. [ ] IDE (e.g., IntelliJ IDEA,)

**Clone the Repository** :

`git clone https://github.com/truecaller/qa-enterprise-sdk-backend-test.git`

`cd tc-sdk-api-automation`

**Install Dependencies** :

`mvn clean install`

**Run all tests using Maven:** :

`mvn test`

**Generate and view the Allure report** :

`allure serve allure-results`
`OR`
`allure serve folder name`

















