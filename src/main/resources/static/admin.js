const usersTableRow = document.getElementById('usersTableRow');
const addNewUserForm = document.getElementById('addNewUserForm');
const firstNameAddValue = document.getElementById('first_name');
const lastNameAddValue = document.getElementById('last_name');
const ageAddValue = document.getElementById('age');
const emailAddValue = document.getElementById('email');
const passwordAddValue = document.getElementById('password');
const editBtnModal = document.getElementById('editBtnModal');
const deleteBtnModal = document.getElementById('deleteBtnModal');
const url = "http://localhost:8080/api/users";
let usersTableRowContent = '';

const renderUsersTable = (users) => {
    users.forEach(user => {
        usersTableRowContent += `
            <tr id="rowId${user.id}">
                <th scope="row" id="userId">${user.id}</th>
                <td id="userFirstName">${user.firstName}</td>
                <td id="userLastName">${user.lastName}</td>
                <td id="userAge">${user.age}</td>
                <td id="userEmail">${user.email}</td>
                <td id="userRoles">${getRoles(user)}</td>
                <td>
                    <button type="button" class="btn btn-info editBtnUsersTable" data-toggle="modal" data-target="#editWindow">Edit</button>
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
            document.getElementById('usersTableTabLink').click();
            addNewUserForm.reset();
        })
});

// edit user
editBtnModal.addEventListener('click', (e) => {
    let id = $("#id_edit").val();
    let newFirstName = $("#first_name_edit").val();
    let newLastName = $("#last_name_edit").val();
    let newAge = $("#age_edit").val();
    let newEmail = $("#email_edit").val();
    let newPassword = $("#password_edit").val();
    let newRoles = $('#role_edit').val();


    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: id,
            firstName: newFirstName,
            lastName: newLastName,
            age: newAge,
            email: newEmail,
            password: newPassword,
            roles: JSON.parse(createJsonWithRoles(newRoles))
        })
    })
        .then(response => response.json())
        .then(() => {
            let rowIdStr = "#rowId" + id;
            let rolesStr = '';
            for (let el of newRoles) {
                rolesStr += el.split('_')[1] + " ";
            }
            $(rowIdStr + " #userFirstName")[0].innerHTML = newFirstName;
            $(rowIdStr + " #userLastName")[0].innerHTML = newLastName;
            $(rowIdStr + " #userAge")[0].innerHTML = newAge;
            $(rowIdStr + " #userEmail")[0].innerHTML = newEmail;
            $(rowIdStr + " #userRoles")[0].innerHTML = rolesStr;
            $("#editWindow").modal('hide')
        })

})

// delete user
deleteBtnModal.addEventListener('click', () => {
    let id = $("#id_delete").val();
    console.log(id);

    fetch(`${url}/${id}`, {
        method: 'DELETE',
    })
        .then(response => response.text())
        .then(() => {
            document.getElementById('rowId' + id).remove();
            $("#deleteWindow").modal('hide');
        })

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

$(function () {
    // fill delete modal window with data
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
    });

    // fill edit modal window with data
    $("#usersTableRow").on("click", "button.editBtnUsersTable", function (e) {
        const id = $(this).parents("tr").find("#userId").text();
        const firstName = $(this).parents("tr").find("#userFirstName").text();
        const lastName = $(this).parents("tr").find("#userLastName").text();
        const age = $(this).parents("tr").find("#userAge").text();
        const email = $(this).parents("tr").find("#userEmail").text();
        const roles = $(this).parents("tr").find("#userRoles").text();

        document.getElementById('editUserForm').reset();

        $("#id_edit").val(id);
        $("#first_name_edit").val(firstName);
        $("#last_name_edit").val(lastName);
        $("#age_edit").val(age);
        $("#email_edit").val(email);
        $("#role_edit").val(roles.split(" "));
    });
});
