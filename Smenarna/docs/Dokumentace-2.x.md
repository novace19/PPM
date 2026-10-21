# Dokumentační zpráva — Jednoduchá směnárna BASIC 2.0

Autor: Vojtěch Nováček  
Vývojová větev: 2.x; modul Android Studia: `basic2`.

## Cíl a realizace

Spinner čerpá EUR, USD, GBP a PLN z arrays.xml. RadioGroup určuje vstup v CZK nebo cizí měně. Kurzy za jednotku jsou 25; 23; 29,5; 5,8 CZK.

Všechny popisky, hlášky, názvy a formáty výstupu jsou ve strings.xml. Ikona je vlastní vektorová grafika; BASIC používá modrozelenou, TOP zlatou variantu. Rozhraní používá světlé pozadí a svislé rozložení ve ScrollView.

## Zpracování vstupu a výpočet

Po stisku tlačítka se čte text z EditText. Prázdný vstup se ohlásí pomocí setError. Money.parse sjednotí desetinnou čárku na tečku, ověří formát a převede řetězec pomocí Double.parseDouble. NumberFormatException zachytává MainActivity. Záporné hodnoty, nečíselné hodnoty a přetečení jsou odmítnuty. Money.convert dělí nebo násobí kurzem. Výsledek zobrazuje parametrizovaný zdroj result_currency se dvěma desetinnými místy a kódem měny.

## Životní cyklus

XML prvky mají vlastní ID a Android obnovuje jejich stav. Výsledek se navíc ukládá v onSaveInstanceState.

## Scénáře pro doložení funkčnosti

| Akce | Očekávaný výsledek |
|---|---|
| Nevyplněná částka → převod | Vyplňte částku! |
| Částka 2500, CZK → EUR v BASIC | 100,00 EUR |
| Částka 100, EUR → CZK v BASIC | 2500,00 CZK |
| Částka 12,50 nebo 12.50 | Přijetí stejné desetinné hodnoty |
| Neplatný vstup / přetečení | Chybová hláška, aplikace nespadne |
| Otočení obrazovky po převodu | Zachování vstupu, směru a výsledku |
| Změna měny a směru | Převod podle příslušné měny a směru |

## Stav ověření a printscreeny

Dokumentace popisuje implementaci; zatím nedokládá úspěšné spuštění na zařízení. Lokální kontrola XML a textových referencí prošla. Lokálně chybí Android SDK a javac; Gradle nebylo možné stáhnout. Sestavení, testy a screenshoty zajišťuje přiložený workflow GitHub Actions, pokud jej GitHub umožní spustit.

Před odevzdáním doplňte skutečné PNG z artefaktu `smenarna-real-screenshots` (nebo z vlastního emulátoru), výsledek sestavení a datum testování. Žádné náhledy zde nejsou vydávány za screenshoty běžící aplikace.

Požadované obrázky: hlavní obrazovka s výsledkem, prázdný vstup s chybou; u 2.x také jiná měna a opačný směr, u TOP navíc lístek, graf a chyba sítě. Ke každému obrázku připojte krátký popisek provedené akce a výsledku.
