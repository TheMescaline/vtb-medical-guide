$(document).ready(function() {
    $('#example').DataTable( {
        ajax: {
            // url: "http://localhost:8080/api/v1/clinics/",
            url: "https://vtb-medical-guide.herokuapp.com/api/v1/clinics/",
            dataSrc: '_embedded.clinicList'
        },
        columns: [
            { data: "clinicName" },
            { data: "medicalServices" },
            { data: "address" },
            { data: "description" }
        ]
    } );
} );