# Dokumentační zpráva — Jednoduchá směnárna TOP 3.0

Autor: Vojtěch Nováček  
Vývojová větev: 3.x; modul Android Studia: `top`.

## Cíl a realizace

Úvodní MainActivity převádí podle skutečně načteného lístku ČNB. RatesActivity obsahuje TabLayout a ViewPager2 se dvěma fragmenty. RatesFragment zobrazuje všechny měny z lístku. ChartFragment umožňuje volbu měny a zobrazuje vlastní Canvas graf s datovými popisky os a skutečnými kalendářními rozestupy. Tlačítko Zpět volá finish(), ImageButton s ikonou křížku volá finishAffinity().

Všechny popisky, hlášky, názvy a formáty výstupu jsou ve strings.xml. Ikona je vlastní vektorová grafika; BASIC používá modrozelenou, TOP zlatou variantu. Rozhraní používá světlé pozadí a svislé rozložení ve ScrollView.

## Zpracování vstupu a výpočet

Po stisku tlačítka se čte text z EditText. Prázdný vstup se ohlásí pomocí setError. Money.parse sjednotí desetinnou čárku na tečku, ověří formát a převede řetězec pomocí Double.parseDouble. NumberFormatException zachytává MainActivity. Záporné hodnoty, nečíselné hodnoty a přetečení jsou odmítnuty. Money.convert dělí nebo násobí kurzem. Výsledek zobrazuje parametrizovaný zdroj result_currency se dvěma desetinnými místy a kódem měny.

## Životní cyklus

XML prvky mají vlastní ID a Android obnovuje jejich stav. Výsledek se navíc ukládá v onSaveInstanceState.

Síťové operace běží v ExecutorService mimo hlavní vlákno, mají connect/read timeout a spojení se uzavírá. ViewModel uchovává načtená data při otočení zařízení. LiveData aktualizuje pouze aktivní pozorovatele; fragmenty používají getViewLifecycleOwner. Pracovní executor se ukončí v onCleared. Při chybě se zobrazí hláška a lze načtení zopakovat; dříve načtený lístek zůstává dostupný a chyba je viditelná. Po ukončení procesu je třeba kurzy znovu načíst. Bez prvního úspěšného načtení je převod zakázán.

Denní parser čte datum, kód, množství a kurz. Výpočet kurz/množství správně zachází i například se 100 JPY. Historický parser hledá sloupec podle měnového kódu, nepředpokládá pevné pořadí sloupců. Graf vynechává nedostupné záznamy, zobrazuje časové rozmezí, počet záznamů a CZK za jednu jednotku. Načítá nejvýše posledních 60 dostupných záznamů.

Zdroj rozhraní: https://www.cnb.cz/cs/casto-kladene-dotazy/Kurzy-devizoveho-trhu-na-www-strankach-CNB/

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
| Odpojení sítě, opakování | Chybová hláška, možnost opakovat |
| Kurzy a graf, přepnutí záložek | Dvě samostatné stránky, historie dle měny |
| Zpět / křížek | Návrat na úvod / ukončení úloh aplikace |

## Stav ověření a printscreeny

Dokumentace popisuje implementaci; zatím nedokládá úspěšné spuštění na zařízení. Lokální kontrola XML a textových referencí prošla. Lokálně chybí Android SDK a javac; Gradle nebylo možné stáhnout. Sestavení, testy a screenshoty zajišťuje přiložený workflow GitHub Actions, pokud jej GitHub umožní spustit.

Před odevzdáním doplňte skutečné PNG z artefaktu `smenarna-real-screenshots` (nebo z vlastního emulátoru), výsledek sestavení a datum testování. Žádné náhledy zde nejsou vydávány za screenshoty běžící aplikace.

Požadované obrázky: hlavní obrazovka s výsledkem, prázdný vstup s chybou; u 2.x také jiná měna a opačný směr, u TOP navíc lístek, graf a chyba sítě. Ke každému obrázku připojte krátký popisek provedené akce a výsledku.
