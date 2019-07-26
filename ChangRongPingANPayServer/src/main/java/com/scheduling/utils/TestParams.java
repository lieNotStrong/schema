package com.scheduling.utils;

/**
 * Interface TestParams
 * Description	说明：
 * Create 2017-04-19 14:26:00
 * @author Benny.YEE
 */
public interface TestParams {

	
    String OPEN_URL = "https://api_uat.orangebank.com.cn/mct1/";
//获取门店支付方式列表
    String PAYLIST = "paylist";
//获取订单列表
    String ORDERLIST = "order";
    String ORDERVIEW = "order/view";
//下单接口
    String PAYORDER = "payorder";
//查询付款状态
    String QUERYPAYSTATUS = "paystatus";
    String PAYCANCEL = "paycancel";
//订单退款接口
    String PAYREFUND = "payrefund";
    String QUERYPAYREFUND = "payrefundquery";
    String SELLTEACCOUNT = "/bill/settleaccount";
//商户对账单下载
    String DOWNLOADBILL = "bill/downloadbill";
//获取用户openid
    String AUTHTOOPEN ="authtoopenid";

    String OPEN_ID = "577493b6543970f56c9064753cb46813";
    String OPEN_KEY = "e49948099e4adffe6c69104e0e834862";
    String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDoYLKYSup2POAg"+
						    "mf2iHwGhUQZSeyyxLvslDcFCRekg1D7w+Y8dybhBOyDvlTbuG15jt8vqqH1xlGTI"+
						    "66yz351uxrUL10UL6Uw+kx1091/BCiRo1AY9frOlIeTnVZy/S2LPWjEefI9o0nOB"+
						    "YJkoJZsaFffX6qXVFkCfAGNMohyuU0gNHw3SXXMBiJ2n2E7GfFf/peAmtTg4zFJO"+
						    "+CrikfSTrL0u5Y2dWQp1KQvkfCMrXaECM24fLc6xxwAdiARC6DjMK8F+ZxovEgb0"+
						    "VObpbZ5pnV2XrKEbiDXQ/DbeNTmELHN8k0S3NUY6ozpu1M3JDqZPhBIDgd/gcZkn"+
						    "FngCRJ95AgMBAAECggEAXZYkF0WEq93UfgzGozZNl8RkAW/uDeXX65JglOpG+5u/"+
						    "RZmcU+jbthm0KAk2OCr5lrt8+qKk8stK08hmo4KZivWoEH7AJg3tUP46zNKb08jb"+
						    "5QQPB1Ex1H2UDL7kA/6+arfuNFMCBrtLHX3j8NFEZ/sU9/ZelzUBDYhAdaqMVoAb"+
						    "fOX7MIZMRIZRhrZCuQlZI0Wz1oGzOf4WB40HEvgRfkGkdC2C+co9tCK7PRcJCiAn"+
						    "Sro175E8NLmYIs+0yUtztUUPSUcVmZ4NfQzM/tD7oyeHREkmBEM6NT62U2PAHyhd"+
						    "FJH23T5Rq80u2Qbzpqv6Fk9aNlBM5uN24umVU7C2oQKBgQD0Hyw7SKOGExbBOOyJ"+
						    "FLpeISsiACokPz7rGwgQJfNfGrbnBLuo7VyxRAX9y01FQrsZL0ZqZ09dSnY6Kqjm"+
						    "MzUQdtF1mteiF9wC6xHcIN9oXzDtDc0geB/hb3zLRIHs5yrCgNci8FjGL0OwBaNN"+
						    "z0vLa02C8Vm5P6rjlclyjK7ZtQKBgQDzrz0i+mCgklD6+cbEA3Z+0b75s5Lg9cu1"+
						    "3eBntZ/+11d2Kf1JCrgrCiRcX1ggOG2ZHmPmi5cs93J6rd+HWInPWJ+j4GUYN5wP"+
						    "S8GxiQun42b3FloT5Zoe4Cj3TmonIHvhFQojYAniKPx3jb3mCLOUyHWjIc5r9zE6"+
						    "Tovth195NQKBgE7k9CqEozRlXuk7OFZk+IYLOiFW5EeqmO7qYYS2fxyxSYMHqI5D"+
						    "h71SOo128pX7pvPQr3Ubxi5kLilGOCeNTQzxGWhkjmO4SkY3KiJ2DT1x5iH2X+Cq"+
						    "ccMtgKtAjKy/WLZbZSvJeSczhzCP4eL3p4sqNnanAVQ5G0VJ1zzJ8ogxAoGAA+4i"+
						    "nUrOfih9995Jb2Xi5l65pstXphswwukmMmYCg5izh2tb826h08fhGEBNao+ebObJ"+
						    "k7FSqd3/0ay2OzeZWWfDg2AeIUrcUH7XS+a68mU/huKsZz+/wZm572srWSAz/0hY"+
						    "loN5BVXF5KO7mVcwlki5ZP0pmCIvgBI+PYF+b7UCgYEA3WpyaYgDSVrE2pcF4+dv"+
						    "qFNIDGcFBGqCyeCaGx4E1WuP0R7y1SFM2miGnbl+te89ZLSzSudoeFq/qS8S3W+M"+
						    "+PuQJlmlFnQ+B/DaEJmKGiiNAqWT9cdohLeYNaJBvk55MdnRJl4dAfkf5PFIf64C"+
						    "WvdDEvhehuV6kub3i8KEvH8=";
   //自己配置公私钥
//    String OPEN_ID = "6c0658c3a8512d187118c9b55d6a3794";
//    String OPEN_KEY = "435f559f5e6abcdf9585b495d52a0e15";
//    
//    String PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCtqGKRoiz4g2IEpK7kVQ7L8HeA"+
//						    "oEJGgqlN0rE7REcCE71UO6tUfGtJIiSAjZASCG6EAFMOvpWyOsjGUz9y7k4cJ+OXzReKkTypNlt8"+
//						    "o7KMCytUHCqgoxXsjVGbaZHIMVWdhkgxgPDpwWwboR3R3U+ZRmKgsnzasi9cUCJCpH7Zmjxp1+3L"+
//						    "O5hAsjEvrawItsxozauCVYM9yHR+9lDKTVhqSz230AzCiL0Nh4sWGMeVEtq3NX/jJm1KO7Sf6sKz"+
//						    "y8O0OoIrgqBokIkH13jaa8XNeYFTTXwq1P5C99LBoY6rFwLJwxY+uYuIFNjAmPvEEdbg769FQcAx"+
//						    "O45f2iDuJX+7AgMBAAECggEAbzy5RwhXPikKlGbZELHbdI9oPSBq07fGs28O0CQASDhUwqwmJ0w7"+
//						    "fPE96IrFioeUI0IB8iPqx+wXx9Jzhi9YIXj7tLVPKxe1f76uA7zoFgThkFMqixNsa8ZqS97n3xCb"+
//						    "AviNLgimSVX45gnijBfzcP9BFia/C2ewJrW9/GpaRxAS3z4ojKhDCCyr+d2nj4BFxpbWYD+3y0wQ"+
//						    "T3wcAphv1gxmPRii4vsKd1m27uupY1cDi3P4uSaB45qRDRQO0Goa30uq6Nghh/eLX3Gfh6AiboGc"+
//						    "5P/Gf3Qo4nz6Ta/i96JZfzNabGNm7Hu64sT4dENDi0Mg05lruyH8IyjsJnOXgQKBgQDb1/GD7zu/"+
//						    "XiLpZEIr/Kbolnm7IBRFFBYqWOzfb0PGa0XyPNf3YS8nyMMZCqO/GJZ9/mTgqgfmE5OxCZB8uY8r"+
//						    "A2nCHzVXotpHPlY/qm/A2/ZO5hAtOwjjIoDxfgsux5BANVTIafdO7LtDtolWgW2tiyK984suUuqu"+
//						    "9MnOYj4n0QKBgQDKN+KcCseG02OqZfYXDfD6DNeH8L5T4NYyqUptfC8k+Htm43MFrH3e0nA0OujG"+
//						    "JQL3mYWv2h1pd1F3Fpob/rgmLvVTWgXAMp5CO3w0bFuspo3JeVWIggO4DW1Dsjf+6CcooPzkoKUj"+
//						    "/tS+fc4Ch6yHf9yHahePCRcK2rn6GlVdywKBgQCK9ONjUUOiog3PF9/7jaayPOqUrKIjR577BVoz"+
//						    "oFBmikeb5Xlck8EKlIs8uMznbNMysClzGAYGFzmzHSiGrYMg3PdsC+3bhsQRZAWAGGqqrF3zYNe4"+
//						    "vyfO6XA3NEQ9PUagTxVX2CLJcDithCMq2oRIvTkod1fx39cW3wLvwIkpAQKBgQChMIrh93W0cywu"+
//						    "17eiCOrdZv8mzS/pjH8xQuChBSxPPAKhsQwuqaBrzTLDb3G9ACOtWiLGWga32vSFCO8vAYuoZCYJ"+
//						    "muiam76q7N/EqdspdFnxD1auyXRNceZ238ji2Arex/TOCX+ZgbfzYDSmr59HIxSWnGrLCWPksxUr"+
//						    "13HUoQKBgQCadzlBUqYo6NzlV0sJXHUeLA28Wk2Bx6KrP115zDOEGTqmtjwRgjP/cRoRe5W9Gqvc"+
//						    "9JB0OMrWNUfJWmX0xjRMFNpuP3sk6bWeKxsGvt2EM4RpgP7z5aUP387TM0axzSJGGMlVD51mb53+"+
//						    "n4Gdby3QmsFZD57yX6n8aSLUxXXYng==";
    
//    String OPEN_ID = "66eb2371165fb08a4ed2cbb4ceb4c23c";
//    String OPEN_KEY = "a901f30b757b93bff53b2aaa28e9389e";
//    String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC3t0+31c881XWp"+
//							"aTK5T6fLMkFStuqFyK7ufkOvlORdcaboyCR6itdxM/XCCpE79WIgHMieDK+CjdkN"+
//							"sk6AXkafl20eqNMZ4ctP9BYFimD57dFJKsRoGNiBU50DDVn/A6/i7Mr826ZabIsC"+
//							"mKi+uX/N1TbSFUpxLTrvZoNH8Q9Gm1ONCqa0RNwWmd19TRu+CCZ6p/CX7SRbKsGr"+
//							"y00+lMnN6aZA2pIx6oBNw8dPSsfL+Iu8HQdOem/dTnczI1x9kq2cJBhsPlU5Jx+9"+
//							"iVEdMP6WCpWIVYAbT8Twa5Qos8ZERWElOizUWsIeeEbXXhgUo2+0LQKmB6t9219M"+
//							"ofIlgOs7AgMBAAECggEAG45NUL+rM+64lU6IIy+FdX72OVfiWWq+gltB/8YPcsyn"+
//							"QmcywYatxfcLN62qFxo5O6PVm48/jA+GrP0/tRVu9mUaIu0jrNAnVLjBzNbysMox"+
//							"CA54Iz0GzG8kL98CzrNY4GqwD7lKfGmneB45wVHFj7QZYu16mvszydDQhCvuJKLK"+
//							"mHZuK//ocTWFbfXgn8OL8zIMbh/a6VjyvXOiQk9xbjkVceAZvVqC0s7uz2mUEIDt"+
//							"MpAUT25zOluEWcjwdSv7Ea/ABkmXYm9KtZo/ebDz7AdKDKdFEqH7uaYhZa/LsYY9"+
//							"knWAOSOhHHtQZ/q1E9C0RzecK+uELe+hZ0XPs9vW0QKBgQDaFadro8ZaFN+VRw/L"+
//							"jqR4zUR971uLFSxBus/5Fw9xUeGbZVqUFf3CP8MeI8RcDapRay6IsXFLrbGjpzYJ"+
//							"Iv8XzSo3//vZTxXngyKmsSUHXA4JoWcx3X6Nc/eip4NuFTUK2YdRfLGfuaNOEMxo"+
//							"zMJTcxpCLtGyGSDrdLOY9p6nBQKBgQDXqAI7KEuOh76WqoMlA/9r38FQzGVnPXSt"+
//							"7CDJI8dK5h239uikbqFEFppf+yCo1mU0jEaZtemqIPUKaScHq7bXPIrj3AuK3Lc8"+
//							"/yeqlHQy5BDo7Z2UjzMGHmPtB7HK/3WdX8aUVVIX0VCZekl3e9BwynpC87QEtdSQ"+
//							"hOANMvVdPwKBgEmSNonElVy8X/T/JR/5vhiWvv1xK0PPX7/I4vJYSjhjdCW1WopM"+
//							"pO/MDOn1DQA1B/kxnGA25E6wiRX5iewpJz+DUjPUIsVbvbr9bUooxqIl9wJete6H"+
//							"l3Lm5Wq08kzH0L4ZRmJCG7YTrTjfx+Wu9fuq//fFza40dfuYwiHfSQR1AoGACsvl"+
//							"eLK2vab8Gqzkrei6U9s64WGqPfXsrnxg09EPvCczG6+ny/uh5TfAeMeoxesd2/id"+
//							"3DooPZUyz1eeVZr6J2JnjarJhSUxo0Me6EJQYu/aDN9AgVx98HN7CzXUZ9ZMPBm8"+
//							"ZlazKnAXOefOX8X6FHt+jXWH8KhtwOJ2GRMRB58CgYEA0pvoic/cd9OtG2sCV9qb"+
//							"/aWi2atSfFvLwRhxIVzamwfz1AjeaomsGGqyrgLVF2ky1o14ilxNLEXY9s/dqnap"+
//							"8/Jp31oPcWsz/l/tqcO5oy6Fi4mSLNIxwquoyl/y0j0FKeW7EmSN8yV/YQLUD73+"+
//							"dTE/WAprweo/oSF/mmKK1Oo=";    
    
    
    
}


