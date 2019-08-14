$(document).ready(function() {
    $('#example').DataTable( {
        ajax: {
            url: "http://localhost:8080/clinics",
            dataSrc: '_embedded.clinicList'
        },
        columns: [
            { data: "clinicName" },
            { data: "medicalServices" },
            { data: "address" }
        ]
    } );
} );