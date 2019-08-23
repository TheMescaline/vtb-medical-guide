const clinicsApiUrl = "https://vtb-medical-guide.herokuapp.com/api/v1/clinics/";
const organizationsApiUrl = "https://search-maps.yandex.ru/v1/?apikey=ce26bd85-deb3-4b43-ad3a-ba3215efdc9a&lang=ru_RU&type=biz&results=1&text=";
const clinicsFullList = new Set();
let geoObjectsCollection;

$(document).ready(function () {
    $('#employee-category').select2({
        placeholder: "Вариант ДМС"
    });
    $('#medical-service').select2({
        placeholder: "Оказываемые услуги"
    });

    $.get({
        url: clinicsApiUrl
    }).done(function (data) {
        initClinicsList(data);

        initSelect(clinicsFullList, 'medicalServices', '#medical-service');
        initSelect(clinicsFullList, 'employeeCategories', '#employee-category');

        ymaps.ready(init);

        function init() {
            geoObjectsCollection = [];
            myMap = new ymaps.Map("map", {
                center: [55.76, 37.64],
                zoom: 10
            }), clusterer = new ymaps.Clusterer({
                preset: 'islands#invertedBlackClusterIcons',
                groupByCoordinates: false,
                clusterDisableClickZoom: true,
                clusterHideIconOnBalloonOpen: false,
                geoObjectHideIconOnBalloonOpen: false
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
            computeGeoPoints(clinicsFullList);
        }
    });

    $('.multiselect').on('change', refreshGeoPoints());
    $('.multiselect').on("select2:unselect", function () {
        $('.multiselect').prop("_type",close);
    });
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

    let sortedResult = Array.from(set).sort();

    sortedResult.forEach(function (val) {
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
        clusterer.removeAll();
        geoObjectsCollection = [];

        computeGeoPoints(filterClinicsVisibleList('#medical-service', 'medicalServices', filterClinicsVisibleList('#employee-category', 'employeeCategories', clinicsFullList)));
    };
}

function computeGeoPoints(clinics) {
    clinics.forEach(function (clinic) {
        let contactInfo = "";
        if (clinic.url) {
            contactInfo += "<a href='" + clinic.url + "'>" + clinic.url + "</a>"
        }
        if (clinic.phone) {
            contactInfo += "<br/>" + clinic.phone;
        }
        const point = new ymaps.Placemark([clinic.x, clinic.y], {
            balloonContentHeader: clinic.clinicName,
            balloonContentBody: clinic.address + "<br/>" + contactInfo,
            balloonContentFooter: clinic.description,
            clusterCaption: clinic.clinicName
        }, {
            preset: 'islands#blackIcon'
        });

        geoObjectsCollection.push(point);
    });
    clusterer.add(geoObjectsCollection);
    myMap.geoObjects.add(clusterer);
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

