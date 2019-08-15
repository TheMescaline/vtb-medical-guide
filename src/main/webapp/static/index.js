$(document).ready(function () {
        let clinicData;

        $.get({
            url: "http://localhost:8080/api/v1/clinics"
        })
            .done(function (data) {


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
                    var myMap = new ymaps.Map("map", {
                        center: [55.76, 37.64],
                        zoom: 10
                    });

                    $(data._embedded.clinicList).each(function () {
                        var thiz = this;
                        if (thiz.x == 0.0 && thiz.y == 0.0) {
                            ymaps.geocode(this.address, {
                                results: 1
                            }).then(function (value) {
                                var firstGeoObject = value.geoObjects.get(0),
                                    coords = firstGeoObject.geometry.getCoordinates(),
                                    bounds = firstGeoObject.properties.get('boundedBy');

                                thiz.x = coords[0];
                                thiz.y = coords[1];

                                $.ajax({
                                    url: "http://localhost:8080/api/v1/clinics/" + thiz.id,
                                    type: "PUT",
                                    data: JSON.stringify(thiz),
                                    contentType: "application/json"
                                });

                                firstGeoObject.options.set('preset', 'islands#blackIcon');
                                myMap.geoObjects.add(firstGeoObject);
                            });
                        } else {
                            let myGeoObject = new ymaps.GeoObject({
                                // Описание геометрии.
                                geometry: {
                                    type: "Point",
                                    coordinates: [thiz.x, thiz.y]
                                },
                                balloonContentHeader: thiz.clinicName,
                                balloonContentBody: thiz.address,
                                balloonContentFooter: thiz.description
                            }, {
                                // Опции.
                                // Иконка метки будет растягиваться под размер ее содержимого.
                                preset: 'islands#blackIcon'
                            });

                            myMap.geoObjects.add(myGeoObject);
                        }


                    })
                }
            });
    }
);



