const clinicsApiUrl = "http://localhost:8080/api/v1/clinics/";
const clinicsFullList = new Set();
var geoObjectsCollection;

$(document).ready(function () {
    $('.js-example-basic-multiple').select2();

    $.get({
        url: clinicsApiUrl
    }).done(function (data) {
        initClinicsList(data);

        initSelect(clinicsFullList, 'medicalServices', '#medical-service');
        initSelect(clinicsFullList, 'employeeCategories', '#employee-category');

        ymaps.ready(init);

        function init() {
            geoObjectsCollection = new ymaps.GeoObjectCollection();
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
                    });
                }
            });
            putGeoPoints(clinicsFullList);
        }
    });

    $('#medical-service').on('change', refreshGeoPoints('medicalServices', '#medical-service'));
    $('#employee-category').on('change', refreshGeoPoints('employeeCategories', '#employee-category'));
});

function initClinicsList(data) {
    $(data._embedded.clinicList).each(function () {
        clinicsFullList.add(this);
    });
}

function initSelect(clinics, clinicOption, selectId) {
    let set = new Set();

    clinics.forEach(function (clinic) {
        $(clinic[clinicOption]).each(function () {
            set.add(this.toString());
        });
    });

    set.forEach(function (val) {
        $(selectId).append(new Option(val));
    });
}

function filterClinicsVisibleList(selectId, clinicOption, clinics) {
    result = new Set();
    if ($(selectId).select2('data').length > 0) {
        $(selectId).select2('data').forEach(function (selection) {
            clinics.forEach(function (clinic) {
                if (clinic[clinicOption].includes(selection.text)) {
                    result.add(clinic);
                }
            })
        });
        return result;
    } else {
        return clinics;
    }
}

function refreshGeoPoints(clinicOption, selectId) {
    return function () {
        myMap.geoObjects.remove(geoObjectsCollection);
        geoObjectsCollection.removeAll();

        putGeoPoints(filterClinicsVisibleList('#medical-service', 'medicalServices', filterClinicsVisibleList('#employee-category', 'employeeCategories', clinicsFullList)));
    };
}

function putGeoPoints(clinics) {
    clinics.forEach(function (clinic) {
        const point = new ymaps.Placemark([clinic.x, clinic.y], {
            balloonContentHeader: "<a href='" + clinic._links.self.href + "'>" + clinic.clinicName + "</a>",
            balloonContentBody: clinic.address,
            balloonContentFooter: clinic.description
        }, {
            preset: 'islands#blackIcon'
        });

        geoObjectsCollection.add(point);
    });
    myMap.geoObjects.add(geoObjectsCollection);
}
