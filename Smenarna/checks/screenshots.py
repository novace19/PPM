"""Skutečné screenshoty z běžícího emulátoru; žádné kreslené náhledy."""
import subprocess as sp
import time
import xml.etree.ElementTree as ET
from pathlib import Path
base=Path(__file__).resolve().parents[1]
out=base/'screenshots';out.mkdir(exist_ok=True)
def adb(*args):return sp.check_output(['adb',*args])
def click(rid):
    import re
    for attempt in range(7):
        adb('shell','uiautomator','dump','/sdcard/window.xml')
        xml=adb('shell','cat','/sdcard/window.xml')
        for n in ET.fromstring(xml).iter('node'):
            if n.get('resource-id','').endswith('/'+rid):
                x1,y1,x2,y2=map(int,re.findall(r'\d+',n.get('bounds')))
                if x2>x1 and y2>y1:
                    adb('shell','input','tap',str((x1+x2)//2),str((y1+y2)//2));return
        # Posouvat v pravém okraji, mimo EditText a jeho výběrové úchyty.
        if attempt==0:adb('shell','input','swipe','1050','500','1050','1500','300')
        else:adb('shell','input','swipe','1050','1500','1050','500','300')
        time.sleep(1)
    shot('failure-'+rid)
    (out/('failure-'+rid+'.xml')).write_bytes(xml)
    raise RuntimeError('Element nenalezen: '+rid)
def shot(name):
    time.sleep(1)
    (out/(name+'.png')).write_bytes(adb('exec-out','screencap','-p'))
adb('shell','wm','density','320')
time.sleep(2)
for module in ['basic1','basic2','top']:
    apk=next((base/module/'build/outputs/apk/debug').glob('*.apk'))
    adb('install','-r',str(apk))
    adb('shell','am','start','-W','-n',f'cz.vspj.smenarna.{module}/cz.vspj.smenarna.MainActivity')
    time.sleep(3)
    click('convert')
    if module!='top':shot(module+'-empty')
    click('amount');adb('shell','input','text','2500');adb('shell','input','keyevent','4')
    if module=='top':time.sleep(18)
    click('convert');shot(module+'-conversion')
    if module!='top':
        click('toCzk');click('convert');shot(module+'-reverse')
    if module=='basic2':
        click('currency')
        adb('shell','uiautomator','dump','/sdcard/window.xml')
        import re
        for n in ET.fromstring(adb('shell','cat','/sdcard/window.xml')).iter('node'):
            if n.get('text')=='USD':
                x1,y1,x2,y2=map(int,re.findall(r'\d+',n.get('bounds')))
                adb('shell','input','tap',str((x1+x2)//2),str((y1+y2)//2));break
        else:raise RuntimeError('V nabídce chybí USD')
        click('toForeign');click('convert');shot('basic2-usd')
    if module=='top':
        click('rates');time.sleep(18);shot('top-rates')
        # Druhá záložka přes její skutečné textové označení.
        adb('shell','uiautomator','dump','/sdcard/window.xml')
        import re
        for n in ET.fromstring(adb('shell','cat','/sdcard/window.xml')).iter('node'):
            if n.get('text','').casefold()=='vývoj kurzu':
                x1,y1,x2,y2=map(int,re.findall(r'\d+',n.get('bounds')))
                adb('shell','input','tap',str((x1+x2)//2),str((y1+y2)//2));break
        time.sleep(18);shot('top-chart')
    adb('shell','am','force-stop',f'cz.vspj.smenarna.{module}')
