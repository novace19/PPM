import subprocess as sp, time, re, xml.etree.ElementTree as ET
def adb(*args): return sp.check_output(['adb', *args])
def click(rid):
    for attempt in range(5):
        adb('shell', 'uiautomator', 'dump', '/sdcard/window.xml')
        xml = adb('shell', 'cat', '/sdcard/window.xml')
        for node in ET.fromstring(xml).iter('node'):
            if node.get('resource-id', '').endswith('/' + rid):
                x1, y1, x2, y2 = map(int, re.findall(r'\d+', node.get('bounds')))
                if x2 > x1 and y2 > y1:
                    adb('shell', 'input', 'tap', str((x1+x2)//2), str((y1+y2)//2))
                    return
        adb('shell', 'input', 'swipe', '900', '1400', '900', '500', '300')
        time.sleep(1)
    raise RuntimeError('Nenalezen prvek: ' + rid)
adb('shell', 'wm', 'density', '320')
adb('install', '-r', 'app/build/outputs/apk/debug/app-debug.apk')
adb('shell', 'am', 'start', '-W', '-n', 'cz.vspj.vizitka/.MainActivity')
time.sleep(3)
click('jmeno')
adb('shell', 'input', 'text', 'Jan%sNovak')
click('vek')
adb('shell', 'input', 'text', '25')
adb('shell', 'input', 'keyevent', '4')
click('souhlas')
click('odeslat')
time.sleep(2)
open('screenshot.png', 'wb').write(adb('exec-out', 'screencap', '-p'))
