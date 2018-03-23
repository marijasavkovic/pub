var validatorUser;
var validatorUserPass;

$(function() {
    validatorUser = $("form[name='user']").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: "Please provide a username"
            },
            password: {
                required: "Please provide a password"
            }
        },
        submitHandler: function(form) {
            form.submit();
        }
    });
  validatorUserPass = $("form[name='pass']").validate({
        rules: {
            password: {
                required: true
            }
        },
        messages: {
            password: {
                required: "Please provide a new password"
            }
        },
        submitHandler: function(form) {
            form.submit();
        }
    });
});

function editUser() {
    var id = $("#id").val();
    var username = $("#username").val();
    var password = $("#password").val();
    if (validatorUser.form()) {
        $.ajax({
            type: "PUT",
            url: "/user",
            data: {
                username: username,
                password: password
            },
            dataType: 'json',
            success: function(data) {
                showSuccessMessage(data);
            },
            error: function(xhr, status, error) {
                showErrorMessage(xhr.responseText);
            }
        });
    }
}

function changePassword() {
    var id = $("#id").val();
    var newp = $("#new-password").val();
    if (validatorUserPass.form()) {
        $.ajax({
            type: "PUT",
            url: "/pass",
            data: {
                password: newp
            },
            dataType: 'json',
            success: function(data) {
                showSuccessMessage(data);
            },
            error: function(xhr, status, error) {
                showErrorMessage(xhr.responseText);
            }
        });
    }
}