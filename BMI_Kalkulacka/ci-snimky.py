import subprocess as sp, time, re, xml.etree.ElementTree as ET
from pathlib import Path
def adb(*args): return sp.check_output(['adb', *args])
def click(rid):
    for attempt in range(5):
        adb('shell','uiautomator','dump','/sdcard/window.xml')
        for n in ET.fromstring(adb('shell','cat','/sdcard/window.xml')).iter('node'):
            if n.get('resource-id','').endswith('/'+rid):
                x1,y1,x2,y2=map(int,re.findall(r'\d+',n.get('bounds')))
                if x2>x1 and y2>y1:
                    adb('shell','input','tap',str((x1+x2)//2),str((y1+y2)//2));return
        adb('shell','input','swipe','900','1500','900','500','300')
        time.sleep(1)
    raise RuntimeError('Nenalezeno: '+rid)
def shot(name):
    time.sleep(2)
    Path('snimky',name+'.png').write_bytes(adb('exec-out','screencap','-p'))
Path('snimky').mkdir(exist_ok=True)
adb('shell','wm','density','320')
adb('install','-r','app/build/outputs/apk/debug/app-debug.apk')
adb('shell','am','start','-W','-n','cz.vspj.bmi/.MainActivity')
time.sleep(3)
shot('01-formular')
click('input_height');adb('shell','input','text','175')
click('input_weight');adb('shell','input','text','70')
adb('shell','input','keyevent','4')
click('btn_calculate')
shot('02-vypocet')
adb('shell','input','swipe','900','500','900','1500','300')
click('btn_info')
shot('03-kategorie')
