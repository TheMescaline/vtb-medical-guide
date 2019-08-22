const clinicsApiUrl = "http://localhost:8080/api/v1/clinics/";
const organizationsApiUrl = "https://search-maps.yandex.ru/v1/?apikey=ce26bd85-deb3-4b43-ad3a-ba3215efdc9a&lang=ru_RU&type=biz&results=1&text=";
const clinicsFullList = new Set();
let geoObjectsCollection;

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
                if ((!clinic.x && !clinic.y)) {
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
                            }).done(function (data) {
                                console.log(data);
                            });
                        });
                    }
                }
            });
            putGeoPoints(clinicsFullList);
        }
    });

    $('.js-example-basic-multiple').on('change', refreshGeoPoints());
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

function refreshGeoPoints() {
    return function () {
        myMap.geoObjects.remove(geoObjectsCollection);
        geoObjectsCollection.removeAll();

        putGeoPoints(filterClinicsVisibleList('#medical-service', 'medicalServices', filterClinicsVisibleList('#employee-category', 'employeeCategories', clinicsFullList)));
    };
}

function putGeoPoints(clinics) {
    clinics.forEach(function (clinic) {
        let hdr = "";
        if (clinic.url) {
            hdr += "<a href='" + clinic.url + "'>" + clinic.clinicName + "</a>"
        } else {
            hdr += clinic.clinicName;
        }

        const point = new ymaps.Placemark([clinic.x, clinic.y], {
            balloonContentHeader: hdr,
            balloonContentBody: clinic.address + "<br/>" + clinic.phone,
            balloonContentFooter: clinic.description
        }, {
            preset: 'islands#blackIcon'
        });

        geoObjectsCollection.add(point);
    });
    myMap.geoObjects.add(geoObjectsCollection);
}

//Deprecated - sends asynchronous PUT request, that can broke data in DB
//Can be used to retrieve organization contact data (url/phone) by it's name
function initializeContactData(clinic) {
    if (clinic.url == null || clinic.phone == null) {
        $.ajax({
            url: organizationsApiUrl + clinic.clinicName,
            type: "GET"
        }).done(function (data) {
            clinic.url = "";
            clinic.phone = "";
            if (data.features.length > 0) {
                clinic.url = data.features[0].properties.CompanyMetaData.url;
                if (data.features[0].properties.CompanyMetaData.Phones.length > 0) {
                    clinic.phone = data.features[0].properties.CompanyMetaData.Phones[0].formatted;
                }
            }
            $.ajax({
                url: clinicsApiUrl + clinic.id,
                type: "PUT",
                data: JSON.stringify(clinic),
                contentType: "application/json"
            }).done(function (data) {
                console.log(data);
            });
        });
    }
}

