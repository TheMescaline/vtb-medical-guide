const cilicsApiUrl = "http://localhost:8080/api/v1/clinics/";

$(document).ready(function () {
    $('.js-example-basic-multiple').select2();

    $.get({
        url: cilicsApiUrl
    }).done(function (data) {

        fillSelect(data._embedded.clinicList, 'medicalServices', '#medical-service');
        fillSelect(data._embedded.clinicList, 'employeeCategories', '#employee-category');

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

function fillSelect(clinics, clinicOption, selectId) {
    let set = new Set();

    $(clinics).each(function () {
        $(this[clinicOption]).each(function() {
            set.add(this.toString());
        });
    });

    set.forEach(function(val) {
        $(selectId).append(new Option(val));
    });
}

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




