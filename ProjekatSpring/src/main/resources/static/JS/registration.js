$(document).ready(function () { 
    var uloga = localStorage.getItem("role");
    if(uloga === "trener") {
        //console.log("Morate se izlogovati da biste se ponovo ulogovali!");
        window.location.href = "trener.html"
    } else if(uloga === "clan") {
        //console.log("Morate se izlogovati da biste se ponovo ulogovali!");
        window.location.href = "clan.html";
    } else if(uloga === "admin") {
        window.location.href = "admin.html";
    } else if (uloga === "odjavljen" || uloga == null) {
        localStorage.setItem("role", "odjavljen");
        uloga = "odjavljen";
    } 
});

$(document).on("submit", "form", function (event) {           // kada je submit-ovana forma za kreiranje novog zaposlenog
    event.preventDefault();                                   // sprecavamo automatsko slanje zahteva da bismo pokupili podatke iz forme

    // preuzimamo vrednosti unete u formi
    var korisnickoIme = $("#UsernameField").val();
    var password = $("#PassField").val();
    var passwordC = $("#ConfirmPassField").val();
    var ime = $("#NameField").val();
    var prezime = $("#SurnameField").val();
    var email = $("#EmailField").val();
    var datumRodjenja = $("#DateField").val();
    var telefon = $("#ContactField").val();
    var uloga  = "clan";
    var active = true;
    if(password !== passwordC) {   
        alert("Passwords do not match!");
        return false;
    } 
    if(isNaN(telefon)) {
        alert("Contact must be a telephone number!");
        return false;
    }
    // kreiramo objekat zaposlenog
    // nazivi svih atributa moraju se poklapati sa nazivima na backend-u
    var noviKorisnik = {
        korisnickoIme,
        password,
        ime,
        prezime,
        email,
        datumRodjenja,
        telefon,
        uloga,
        active
    }
    console.log(noviKorisnik);
    // ajax poziv
    $.ajax({
        type: "POST",                                               // HTTP metoda je POST
        url: "http://localhost:8080/api/registracija/clan",                 // URL na koji se šalju podaci
        dataType: "json",                                           // tip povratne vrednosti
        contentType: "application/json",                            // tip podataka koje šaljemo
        data: JSON.stringify(noviKorisnik),                          // u body-ju šaljemo novog zaposlenog (JSON.stringify() pretvara JavaScript objekat u JSON)
        success: function (res) {                                   // ova f-ja se izvršava posle uspešnog zahteva
            console.log(res);
            if(res.uloga == "username") {
                alert("Korisnicko ime vec postoji!");
            } else if(res.uloga == "email") {
                alert("Email adresa vec postoji!");
            } else if(res.uloga == "broj") {
                alert("Broj telefona vec postoji!");
            } else { 
                alert("Korisnik " + noviKorisnik.korisnickoIme + " je uspesno kreiran!");
                window.location.href = "login.html";
            }
        },
        error: function () {                                        // ova f-ja se izvršava posle neuspešnog zahteva
            alert("Greška!");
        }
    });
    
});

/*function test() {
    console.log("hello");
}*/