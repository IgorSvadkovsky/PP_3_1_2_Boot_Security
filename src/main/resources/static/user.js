let authenticatedUserTableRowContent = '';
const url = "http://localhost:8080/user/api/user";

const renderAuthenticatedUserTable = (user) => {
    authenticatedUserTableRowContent += `
        <tr id="rowId${user.id}">
            <th scope="row" id="userId">${user.id}</th>
            <td id="userFirstName">${user.firstName}</td>
            <td id="userLastName">${user.lastName}</td>
            <td id="userAge">${user.age}</td>
            <td id="userEmail">${user.email}</td>
            <td id="userRoles">${getRoles(user)}</td>
        </tr>
        `;
    document.getElementById('authenticatedUserTableRow').innerHTML = authenticatedUserTableRowContent;
}

// show authenticated user's table
fetch(url)
    .then(response => response.json())
    .then(user => renderAuthenticatedUserTable(user))

function getRoles(user) {
    let userRoles = "";
    for (let role of user.roles) {
        if (role.name) {
            userRoles += role.name.replace("ROLE_", "") + " ";
        }
    }
    return userRoles;
}