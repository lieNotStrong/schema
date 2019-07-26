/**
 * @Filename: TLinxAESCoder.java
 * @Author锛歝aiqf
 * @Date锛�016-4-12
 */
package com.scheduling.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Class: TLinxAESCoder.java
 * @Description: AES加解密类
 * @Author：caiqf
 * @Date：2016-4-12
 */
public class TLinxAESCoder {
	private static String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static String KEY_ALGORITHM = "AES";

/**
 * 解密
 * @param sSrc
 * @param sKey
 * @return
 * @throws Exception
 */
	public static String decrypt(String sSrc, String sKey) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes("ASCII"), KEY_ALGORITHM);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(2, skeySpec);
		byte[] encrypted1 = hex2byte(sSrc);
		byte[] original = cipher.doFinal(encrypted1);
		return new String(original);
	}
	
	/**
	 * 加密
	 * @param sSrc
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String sSrc, String sKey) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes("ASCII"), KEY_ALGORITHM);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(1, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
		return byte2hex(encrypted);
	}

	private static byte[] hex2byte(String strhex) {
		if (strhex == null)
			return null;

		int l = strhex.length();
		if (l % 2 == 1)
			return null;

		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; ++i)
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);

		return b;
	}

	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; ++n) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}

		return hs.toUpperCase();
	}

	public static void main(String[] args) {
		String data = "AEBE77B6088AD225CC7C53FCA90A2C8D12F22F881B7EFF5E11E7C1DD531D92EE78A310C4E12A2C9AF45CDC9C8EAB893E5458B275D81474B77188A5762E62E18D34179096E20806B36003EBA37D7F8FE042049496339280E9825A6D623FA2D469A576DA6F6B8F7F7EBC79B669826AEA65CA6C4F7948A449002958307868334568AEEBD7708D66B1AD62576946E8D64A632825D397DE8E52C11E17E9EDC07C1BF9CBDBA30E7468DC67A704EA83F484AE8209EF0B9634C92484C713461C6D563CAE59E68D6E32DF3FF5FA68580EE95B96FB6A9AA422AB792365DE5B920A7FED623F562EB7544F81553D8B6288093308407386577032AD26F5A32A3FF422CB797FDB33ED5AA1F04ED837387481247993F781DC02939A04E1DAE81389526707825B91E0CAFA53D906143C608CABA197AD5CDCCABCCF3CA8902964EA8485582F4EA65CC6746FAB8527427ED370D95BC902106CA900F5E900AAB35761AEF4B3E709FADBB766F42BA100E6220157BAC8EEF1A19DB798E209446535890FE5811DBFC3F9BAC39CE0E0A1AF9DD671275D1E559E3A765A449DCE886C7A33AF045100CB71AAA5C39EBA6CA44307E0583AFECCAC0100FB6E9600C1D38E85C565664DD7B6865CAED1E66A583EB7F5BE43D10F6B50FE57EB82C81A14CF1133062629A1B62D27B024C34E3749ACF9AB85D245F9A433FF16715982FCA8831E4997F94B1CA1AF01334954A3EAC7D5A9908FA4F6E051CB4A7C0B0DEE169845F43553BE31E13F08C555B926D444832E3C74958356266CB6A0F9049C991A651AC50A86AD74303272747DFCFBDDF1F009F75769A63C5432635FCB91BDE7476AB4EA063E3A41F9FF9CE721A2C63337ABBD0D4282D8BAFEE6FA8675B4FD4E5623076A1BD937112EC0B9CA58883FCDCBB08C383545BCACF20421F32E99A4FDB751AE4C7322F8FDBAB26308FB5385ACA1468E2E10A4EC52D0808DA90B86066BACAE71A688A3A0BE9FFE32C7F35F5BE608BC18473308DED21D2D74EE7C2EA9144AA59C3E2F9456EAB58D215278A6857FEFCA7B6D4C80A0B8EA466A36D2227001DB9B7E8BAD569C31055FDE0B177C4C77AE810AC73F5C0E6BC22069C0679CD6DCED05F742EE284E3E2D09B791AACCB237285B31937B6CDF6992882C359902D83F262BEFE3167C1E154055EFF0B5F0BBE92BF705501CE0AB9D8957D50BD57FE49E3E4A47069CC9516DCD165C8F5712645A6F5CBA15F5E2E439F4147069BE0B3E5E0720BEEB271032A2AA5CFC2A2BB612D22CC38DB0242062E8CB5DF4E6D10E54A3EAC7D5A9908FA4F6E051CB4A7C0B3FD1ED31C5053199EA83B4B2E585E03090C967F5BE3397194EA90572CBFE1A23298271482473E9BCD0A41598EB7F4FE81E2F168EAB08C623782CB239D3A773049D8CAF370A477D7B02274824D18DBF42671BAA0401BF486213D94697099FE26F5C7E67483015273F6980C1341601A6CF7613A0F8DB61D46C34BB5F04FD0F9EF6A71A3AE2F545E4973DC5E38A3F679BA6BDA40E38204E2BA355EEAD7AE04C51DE20E59DF87888677DF3A12E97436ADAF567CAFDC73176A51BB2BFA68B6C303C09DF3FCCC9A82CFE0A6D9B68E9EBB5C691B86B5F67B2E04C2669848DBB2F4BCF06B798E209446535890FE5811DBFC3F9BA9024F9C0368162E23C33FF68C7316934";
		String str = null;
		try {
			str = decrypt(data,"e7d328330921d6b26824bc67287bbf83");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("str---"+str);
	}
}
