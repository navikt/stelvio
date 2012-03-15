#! /usr/bin/python
'''
Color library version 1.0
@author test@example.com
'''

#main method
def __custom(code=0, *text): return '\033[%sm%s\033[0m' % (code, ' '.join(text))

#spesial methods
def underline(*text): return __custom(21, *text)

#regular colors
def black(*text): return __custom(30, *text)
def red(*text): return __custom(31, *text)
def green(*text): return __custom(32, *text)
def yellow(*text): return __custom(33, *text)
def blue(*text): return __custom(34, *text)
def magenta(*text): return __custom(35, *text)
def purple(*text): return magenta(*text)
def cyan(*text): return __custom(36, *text)
def grey(*text): return __custom(37, *text)

#backgroud colors
def blackBG(*text): return __custom(40, *text)
def redBG(*text): return __custom(41, *text)
def greenBG(*text): return __custom(42, *text)
def yellowBG(*text): return __custom(43, *text)
def blueBG(*text): return __custom(44, *text)
def magentaBG(*text): return __custom(45, *text)
def purpleBG(*text): return magentaBG(*text)
def cyanBG(*text): return __custom(46, *text)
def greyBG(*text): return __custom(47, *text)

#Light colors
def greyLight(*text): return __custom(90, *text)
def redLight(*text): return __custom(91, *text)
def greenLight(*text): return __custom(92, *text)
def yellowLight(*text): return __custom(93, *text)
def blueLight(*text): return __custom(94, *text)
def magentaLight(*text): return __custom(95, *text)
def purpleLight(*text): return magentaLight(*text)
def cyanLight(*text): return __custom(96, *text)

#light backgroud colors
def greyLBG(*text): return __custom(100, *text)
def redLBG(*text): return __custom(101, *text)
def greenLBG(*text): return __custom(102, *text)
def yellowLBG(*text): return __custom(103, *text)
def blueLBG(*text): return __custom(104, *text)
def magentaLBG(*text): return __custom(105, *text)
def purpleLBG(*text): return magentaLBG(*text)
def cyanLBG(*text): return __custom(106, *text)

if __name__ == '__main__':
        g = globals().copy()
        for k in sorted(g).keys():
                if not k.startswith("__"):
                        print "lib.color.%s('Colored *text'): %s" % (k, g[k]('Colored *text'))
