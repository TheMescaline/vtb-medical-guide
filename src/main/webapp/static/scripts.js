$(document).ready(function () {
        let clinicData;

        $.get({
            url: "http://localhost:8080/clinics"
        })
            .done(function (data) {

                ymaps.ready(init);

                function init() {
                    var myMap = new ymaps.Map("map", {
                        center: [55.76, 37.64],
                        zoom: 10
                    });

                    $(data._embedded.clinicList).each(function() {
                        ymaps.geocode(this.address, {
                            results: 1
                        }).then(function (value) {
                            var firstGeoObject = value.geoObjects.get(0),
                                coords = firstGeoObject.geometry.getCoordinates(),
                                bounds = firstGeoObject.properties.get('boundedBy');

                            firstGeoObject.options.set('preset', 'islands#darkBlueDotIconWithCaption');
                            // Получаем строку с адресом и выводим в иконке геообъекта.
                            firstGeoObject.properties.set('iconCaption', firstGeoObject.getAddressLine());

                            // Добавляем первый найденный геообъект на карту.
                            myMap.geoObjects.add(firstGeoObject);
                        });
                    })
                }

            });


    }
);



