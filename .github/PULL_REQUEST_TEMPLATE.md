## 🔍 Description
## ✅ Pull Request Checklist


<!-- Briefly describe what this PR does and the motivation behind it -->
- [ ] New test cases added
- [ ] Existing tests updated
- [ ] Deletion / Disabling the Existing Test cases
- [ ] Test framework changes
- [ ] Rest client layer changes 
- [ ] Added Utilities / Config changes 
- [ ] Bug fixes of test cases
- [ ] CI/CD enhancements or fixes related pipeline

---

## 🎯 Related JIRA / Ticket

- JIRA/Ticket ID: [PROJ-XXX](https://jira.example.com/browse/PROJ-XXX)

---

### 🔍 Summary Related to PR :
<!-- High-level overview of what this PR changes in the test suite -->
- 

---


## 🧪 Test Execution

| Test Type                             | Status                            |
|---------------------------------------|-----------------------------------|
| New Tests Added                       | ✅ / ❌                             |
| Existing Tests Run                    | ✅ / ❌                             |
| Environment                           | `dev` / `qa` / `staging` / `prod` |
| Local Run                             | ✅ / ❌                             |
| CI Pipeline / Github Actions          | ✅ / ❌                             |
| Allure Report Generated (Local / CI ) | ✅ / ❌                             |

### 🔧 Code Quality
- [ ] Code follows the project structure and naming conventions
- [ ] No hardcoded values (base URLs, auth tokens, expected result etc.)
- [ ] No sensitive information (API keys, credentials) in code
- [ ] Code is modular and reusable (e.g., specs, utils, request builders)
- [ ] Proper assertions are included (status code, body, headers, schema)
- [ ] Test data is handled dynamically or through data providers etc
- [ ] Code changes for running in local is reverted and property file is updated only with url
- [ ] I have performed a self-review of my own code
- [ ] keep it Clean. Remove console logs, commented-out code, unused variables , unused imports , extra Spaces 

### 📦 **Code Merging Strategy / Rebase Checklist**
**Important: Please ensure the following before Raising the Pull request:**
- [ ] I have rebased this branch on the latest main / master branch.
- [ ] I have resolved all merge conflicts, if any.
- [ ] I have tested the changes again after rebasing to main branch to ensure nothing is broken.
