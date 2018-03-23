var validatorDrink;
$.validator.addMethod("url2", function(value, element) {
    return this.optional(element) || /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)*(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value);
}, $.validator.messages.url);
$(function() {
    validatorDrink = $("form[name='drink']").validate({
        rules: {
            name: {
                required: true
            },
            price: {
                required: true,
                number: true
            },
        },
        messages: {
            name: {
                required: "Please provide a name"
            },
            price: {
                required: "Please provide price"
            }
        },
        submitHandler: function(form) {
            $('[name="optradio"]').prop("disabled", true);
            form.submit();
        }
    });
});

$(document).ready(function() {
    test();
    $("input[name='optradio']").change(function() {
        test();
    });
});

function test() {
    var option = $('input[name=optradio]:checked').val();
    var img = $("#drink-img").attr('src');
    if (option == "upload") {
        $("#input-picture").html('<input type="file" id="pic-upload" name="file" accept="image/*" />');
    } else if (img != undefined) {
        if (img.indexOf("http") < 0) {
            img = location.protocol + '//' + location.host + img;
        }
        $("#input-picture").html('<input type="text" id="pic-url" name="url" placeholder="Image url" value="' + img + '"/>');
    } else {
        $("#input-picture").html('<input type="text" id="pic-url" name="url" placeholder="Image url"/>');
    }
}

function searchDrinks(text) {
    $.ajax({
        type: "GET",
        url: "/drinks",
        data: {
            text: text
        },
        dataType: 'json',
        success: function(data) {
            var tableData = "";
            if (data != null) {
                for (var i = 0; i < data.length; i++) {
                    tableData += "<tr>";
                    tableData += "<tr id='" + data[i].id + "' onclick='getDrink(this.id)' class='clickable-row'>";
                    tableData += "<td>" + data[i].id + "</td>";
                    tableData += "<td>" + data[i].name + "</td>";
                    tableData += "<td>" + data[i].tname + "</td>";
                    tableData += "<tr>";
                }
            }
            $("#table-drinks tbody").html(tableData);
        },
        error: function(xhr, status, error) {
            showErrorMessage(xhr.responseText);
        }
    });
}

function editDrink() {
    if (validatorDrink.form()) {
        sendEditRequest();
    }

}

function sendEditRequest() {
    var params = new FormData();
    params.append("id", $("#id").val());
    params.append("name", $("#name").val());
    params.append("price", $('#price').val());
    params.append("drink_type", $("#drink_type").val());
    $.ajax({
        type: "PUT",
        url: "/drink",
        data: params,
        dataType: 'json',
        processData: false,
        contentType: false,
        success: function(data) {
            showSuccessMessage(data.message);
        },
        error: function(xhr, status, error) {
            showErrorMessage(xhr.responseText);
        }
    });

}

function deleteDrink(id) {
    $.ajax({
        type: "DELETE",
        url: "/drink",
        data: {
            id: id
        },
        dataType: 'json',
        success: function(data) {
            window.location = "/drinks";
        },
        error: function(xhr, status, error) {
            showErrorMessage(xhr.responseText);
        }
    });
}

function getDrink(id) {
    window.location = "/drink/" + id;
}
