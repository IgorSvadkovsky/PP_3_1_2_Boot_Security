const usersTableRow = document.getElementById('usersTableRow');
const addNewUserForm = document.getElementById('addNewUserForm');
const firstNameAddValue = document.getElementById('first_name')
const lastNameAddValue = document.getElementById('last_name')
const ageAddValue = document.getElementById('age')
const emailAddValue = document.getElementById('email')
const passwordAddValue = document.getElementById('password')
const url = "http://localhost:8080/api/users";
let output = '';

const renderUsersTable = (users) => {
    users.forEach(user => {
        output += `
            <tr>
                <th scope="row">${user.id}</th>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${getRoles(user)}</td>
                <td>
                    <button type="button" class="btn btn-info" data-toggle="modal">Edit</button>
                </td>
                <td>
                    <button type="button" class="btn btn-danger" data-toggle="modal">Delete</button>
                </td>
            </tr>
            `;
    })
    usersTableRow.innerHTML = output;
}

// show all users
fetch(url)
    .then(response => response.json())
    .then(users => renderUsersTable(users))

function getRoles(user) {
    let userRoles = "";
    for (let role of user.roles) {
        if (role.name) {
            userRoles += role.name.replace("ROLE_", "") + " ";
        }
    }
    return userRoles;
}

// add new user
addNewUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstName: firstNameAddValue.value,
            lastName: lastNameAddValue.value,
            age: ageAddValue.value,
            email: emailAddValue.value,
            password: passwordAddValue.value,
            roles: JSON.parse(createJsonWithRoles($('#role').val()))
        })
    })
        .then(response => response.json())
        .then(data => {
            const dataArr = [];
            dataArr.push(data);
            renderUsersTable(dataArr);
        })
})

function createJsonWithRoles(array) {
    let result = [];
    let id;
    let name;
    let role;

    for (let roleSelect of array) {
        const selectArr = roleSelect.split("&&");
        id = selectArr[0];
        name = selectArr[1];
        role = {
            id,
            name
        }
        result.push(role);
    }

    return JSON.stringify(result);
}