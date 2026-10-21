# Jednoduchá směnárna

Autor: Vojtěch Nováček. Android aplikace v Javě, tři samostatné instalovatelné varianty.

## Otevření v Android Studiu

Otevřete přímo složku **Smenarna** (obsahuje settings.gradle), ne celý repozitář PPM. Použijte JDK 17 a nainstalujte Android SDK 35. Po synchronizaci Gradlu vyberte modul basic1, basic2 nebo top a stiskněte Run. Aplikace podporují Android 6.0 a novější a mají rozdílná applicationId, takže lze instalovat všechny najednou.

| Vývojová větev | Modul | Odevzdávaná verze |
|---|---|---|
| 1.x | basic1 | BASIC 1.0, CZK ↔ EUR |
| 2.x | basic2 | BASIC 2.0, EUR / USD / GBP / PLN |
| 3.x | top | TOP 3.0, ČNB, aktivity, fragmenty a graf |

Všechny větve obsahují společný projekt; pro danou fázi se spouští uvedený samostatný modul. Ve větvi main jsou všechny varianty pohromadě. Původní BMI_Kalkulacka je samostatný projekt.

## Funkce

Desetinnou částku lze zadat s čárkou i tečkou. Prázdný vstup, záporné hodnoty, neplatná čísla i přetečení se odmítnou. Výsledek se formátuje na dvě desetinná místa v českém prostředí. Kurzy BASIC jsou výukové konstanty: EUR 25, USD 23, GBP 29,5 a PLN 5,8 CZK. Nákup/prodej označuje směr převodu CZK z pohledu uživatele; nejsou použity různé nákupní a prodejní sazby.

TOP používá veřejné TXT rozhraní ČNB bez klíče: poslední zveřejněný denní lístek a roční historii. Datum lístku je uvedeno v UI; při víkendech či před zveřejněním jde o poslední dostupný pracovní den. Graf ukazuje posledních nejvýše 60 zveřejněných záznamů. Na začátku roku doplní historii z předchozího roku. Kurzy jsou normalizovány na jednu jednotku měny. Data ČNB jsou referenční, nejde o nabídku skutečné směny.

Zdroj a specifikace: https://www.cnb.cz/cs/casto-kladene-dotazy/Kurzy-devizoveho-trhu-na-www-strankach-CNB/

## Ověření a screenshoty

`bash checks/check.sh` kontroluje čistou Java logiku. `bash gradlew :basic1:assembleDebug :basic2:assembleDebug :top:assembleDebug` sestaví aplikace. GitHub Actions přidává Android Lint a následně pořídí skutečné screenshoty v emulátoru; výsledné APK, reporty a PNG jsou v artefaktech příslušného běhu. Screenshoty po stažení doplňte do dokumentací v docs.

Při přípravě v pracovním prostředí byly ověřeny XML a reference na textové zdroje. Lokální sestavení a Java testy nebylo možné dokončit: prostředí nemá Android SDK ani javac a síť neumožnila stažení Gradlu. Úspěch sestavení, lint a běh aplikací musí potvrdit GitHub Actions nebo Android Studio; tento soubor je nepředstírá.
