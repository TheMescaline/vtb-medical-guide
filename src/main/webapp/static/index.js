const cilicsApiUrl = "http://localhost:8080/api/v1/clinics/";

$(document).ready(function () {
    $.get({
        url: cilicsApiUrl
    }).done(function (data) {
        var employeeCategoriesSet = new Set();
        $(data._embedded.clinicList).each(function () {
            $(this.employeeCategories).each(function () {
                employeeCategoriesSet.add(this.toString());
            })
        });
        employeeCategoriesSet.forEach(function (val) {
            $("#employee-category").append("<option value='" + val.toString() + "'>" + val.toString() + "</option>")
        });

        var medicalServicesSet = new Set();
        $(data._embedded.clinicList).each(function () {
            $(this.medicalServices).each(function () {
                medicalServicesSet.add(this.toString());
            })
        });
        medicalServicesSet.forEach(function (val) {
            $("#medical-service").append("<option value='" + val.toString() + "'>" + val.toString() + "</option>")
        });

        $('.js-example-basic-multiple').select2();

        ymaps.ready(init);

        function init() {
            myMap = new ymaps.Map("map", {
                center: [55.76, 37.64],
                zoom: 10
            });

            $(data._embedded.clinicList).each(function () {
                let thiz = this;
                if (!thiz.x && !thiz.y) {
                    ymaps.geocode(this.address, {
                        results: 1
                    }).then(function (value) {
                        const coords = value.geoObjects.get(0).geometry.getCoordinates();

                        thiz.x = coords[0];
                        thiz.y = coords[1];

                        $.ajax({
                            url: cilicsApiUrl + thiz.id,
                            type: "PUT",
                            data: JSON.stringify(thiz),
                            contentType: "application/json"
                        });

                        putPlacemark(thiz);
                    });
                } else {
                    putPlacemark(thiz);
                }

            });
        }
    });
});

function putPlacemark(clinic) {
    const point = new ymaps.Placemark([clinic.x, clinic.y], {
        balloonContentHeader: clinic.clinicName,
        balloonContentBody: clinic.address,
        balloonContentFooter: clinic.description
    }, {
        preset: 'islands#blackIcon'
    });

    myMap.geoObjects.add(point);
}




