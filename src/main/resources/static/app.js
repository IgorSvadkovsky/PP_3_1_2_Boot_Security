const usersTableRow = document.getElementById('usersTableRow');
const addNewUserForm = document.getElementById('addNewUserForm');
const firstNameAddValue = document.getElementById('first_name')
const lastNameAddValue = document.getElementById('last_name')
const ageAddValue = document.getElementById('age')
const emailAddValue = document.getElementById('email')
const passwordAddValue = document.getElementById('password')
const deleteBtnModal = document.getElementById('deleteBtnModal');
const url = "http://localhost:8080/api/users";
let usersTableRowContent = '';

const renderUsersTable = (users) => {
    users.forEach(user => {
        usersTableRowContent += `
            <tr>
                <th scope="row" id="userId">${user.id}</th>
                <td id="userFirstName">${user.firstName}</td>
                <td id="userLastName">${user.lastName}</td>
                <td id="userAge">${user.age}</td>
                <td id="userEmail">${user.email}</td>
                <td id="userRoles">${getRoles(user)}</td>
                <td>
                    <button type="button" class="btn btn-info editBtnUsersTable" data-toggle="modal">Edit</button>
                </td>
                <td>
                    <button type="button" class="btn btn-danger deleteBtnUsersTable" data-toggle="modal" data-target="#deleteWindow">Delete</button>
                </td>
            </tr>
            `;
    })
    usersTableRow.innerHTML = usersTableRowContent;
}

// show all users
fetch(url)
    .then(response => response.json())
    .then(users => renderUsersTable(users))

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
});

// delete user
deleteBtnModal.addEventListener('click', () => {
    let id = $("#id_delete").val();
    console.log(id);

    // $("#usersTableRow").empty();

    fetch(`${url}/${id}`, {
        method: 'DELETE',
    })
        .then(response => response.text())
        // .then(() => location.reload())


        // .then(() => {
        //     // $("#usersTable").load("admin.html #usersTable");
        //     $(".table").load(location.href + ' .table');
        // })


});

function getRoles(user) {
    let userRoles = "";
    for (let role of user.roles) {
        if (role.name) {
            userRoles += role.name.replace("ROLE_", "") + " ";
        }
    }
    return userRoles;
}

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

// fill delete modal window with data
$(function () {
    $("#usersTableRow").on("click", "button.deleteBtnUsersTable", function (e) {
        const id = $(this).parents("tr").find("#userId").text();
        const firstName = $(this).parents("tr").find("#userFirstName").text();
        const lastName = $(this).parents("tr").find("#userLastName").text();
        const age = $(this).parents("tr").find("#userAge").text();
        const email = $(this).parents("tr").find("#userEmail").text();
        const roles = $(this).parents("tr").find("#userRoles").text();

        $("#id_delete").val(id);
        $("#first_name_delete").val(firstName);
        $("#last_name_delete").val(lastName);
        $("#age_delete").val(age);
        $("#email_delete").val(email);
        $("#role_delete").val(roles.split(" "));

        // var element = document.getElementById('role_delete');
        // var values = roles.split(" ");
        // for (var i = 0; i < element.options.length; i++) {
        //     element.options[i].selected = values.indexOf(element.options[i].value) >= 0;
        // }
    });
});
