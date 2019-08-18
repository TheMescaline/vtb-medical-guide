const clinicsApiUrl = "http://localhost:8080/api/v1/clinics/";
const clinicsFullList = new Set();
clinicsVisibleList = new Set();

$(document).ready(function () {
    $('.js-example-basic-multiple').select2();

    $.get({
        url: clinicsApiUrl
    }).done(function (data) {
        initClinicsList(data);

        fillSelect(clinicsFullList, 'medicalServices', '#medical-service');
        fillSelect(clinicsFullList, 'employeeCategories', '#employee-category');

        ymaps.ready(init);

        function init() {
            myMap = new ymaps.Map("map", {
                center: [55.76, 37.64],
                zoom: 10
            });

            clinicsFullList.forEach(function (clinic) {
                if (!clinic.x && !clinic.y) {
                    ymaps.geocode(clinic.address, {
                        results: 1
                    }).then(function (value) {
                        const coords = value.geoObjects.get(0).geometry.getCoordinates();

                        clinic.x = coords[0];
                        clinic.y = coords[1];

                        $.ajax({
                            url: clinicsApiUrl + clinic.id,
                            type: "PUT",
                            data: JSON.stringify(clinic),
                            contentType: "application/json"
                        });

                        // putPlacemark(clinic);
                    });
                }
                // } else {
                //     putPlacemark(clinic);
                // }
            });
            putPlacemarks(clinicsVisibleList);
        }
    });

    $('#medical-service').on('select2:select', refreshGeoPoints());
    $('#medical-service').on('select2:unselect', refreshGeoPoints());
});

function initClinicsList(data) {
    $(data._embedded.clinicList).each(function () {
        clinicsFullList.add(this);
        clinicsVisibleList.add(this);
    });
}

function refreshGeoPoints() {
    return function () {
        clinicsVisibleList = new Set([...clinicsFullList]);
        $('#medical-service').select2('data').forEach(function (selection) {
            clinicsVisibleList.forEach(function (clinic) {
                if (!clinic.medicalServices.includes(selection.text)) {
                    clinicsVisibleList.delete(clinic);
                }
            })
        });

        //TODO Fix map geoObjects refreshing, now it's not removing all points before putPlacemarks function
        myMap.geoObjects.each(function (geoObject) {
            myMap.geoObjects.remove(geoObject);
        });
        putPlacemarks(clinicsVisibleList);
    };
}

function fillSelect(clinics, clinicOption, selectId) {
    let set = new Set();

    clinics.forEach(function (clinic) {
        $(clinic[clinicOption]).each(function() {
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

function putPlacemarks(clinics) {
    clinics.forEach(function (clinic) {
        const point = new ymaps.Placemark([clinic.x, clinic.y], {
            balloonContentHeader: clinic.clinicName,
            balloonContentBody: clinic.address,
            balloonContentFooter: clinic.description
        }, {
            preset: 'islands#blackIcon'
        });

        myMap.geoObjects.add(point);
    })
}
