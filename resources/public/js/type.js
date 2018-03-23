var validatorType;
$(function() {
    validatorType = $("form[name='type']").validate({
        rules: {
            name: {
                required: true
            }
        },
        messages: {
            name: {
                required: "Please provide a name"
            }

        }
    });
});

function searchTypes(text) {
    $.ajax({
        type: "GET",
        url: "/types",
        data: {
            text: text
        },
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        success: function(data) {
            var tableData = "";
            if (data != null) {
                for (var i = 0; i < data.length; i++) {
                    tableData += "<tr id='" + data[i].id + "' onclick='getType(this.id)' class='clickable-row'>";
                    tableData += "<td>" + data[i].id + "</td>";
                    tableData += "<td>" + data[i].name + "</td>";
                    tableData += "<tr>";
                }
            }
            $("#table-type tbody").html(tableData);
        },
        error: function(xhr, status, error) {
            showErrorMessage(xhr.responseText);
        }
    });
}

function getType(id) {
    window.location = "/type/" + id;
}

function editType(id) {
    if (validatorType.form()) {
        var name = $("#name").val();
        $.ajax({
            type: "PUT",
            url: "/type",
            data: {
                id: id,
                name: name
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

function deleteDrinkFromType(id) {
    $.ajax({
        type: "DELETE",
        url: "/drink",
        data: {
            id: id
        },
        dataType: 'json',
        success: function(data) {
            $("#" + id).remove();
            showSuccessMessage(data);
        },
        error: function(xhr, status, error) {
            showErrorMessage(xhr.responseText);
        }
    });
}
