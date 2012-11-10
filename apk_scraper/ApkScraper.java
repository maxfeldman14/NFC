import java.net.*;
import java.io.*;

import com.gc.android.market.api.*;
import com.gc.android.market.api.model.Market.GetAssetResponse.InstallAsset;
import com.gc.android.market.api.model.Market.*;

/* Lots of this is copied from
 * http://code.google.com/p/android-market-api/wiki/HowDownloadApps
 */

class ApkScraper
{
    public static void main(String[] args)
    {
        String login = null;
        String password = null;
        String androidId = null;
        String assetId = null;

        if (args.length != 4)
        {
            System.err.println("Wrong number of arguments");
            System.err.println("Usage: ApkScraper login password androidId assetId");
            System.exit(1);
        }
        else
        {
            login = args[0];
            password = args[1];
            androidId = args[2];
            assetId = args[3];
        }

        try {
            MarketSession session = new MarketSession(true);

            session.login(login, password, androidId);

            GetAssetResponse gar = session.queryGetAssetRequest(assetId);
            InstallAsset ia = gar.getInstallAsset(0);
            String cookieName = ia.getDownloadAuthCookieName();
            String cookieValue = ia.getDownloadAuthCookieValue();

            System.out.println("cookieName: " + cookieName);
            System.out.println("cookieValue: " + cookieValue);

            URL url = new URL(ia.getBlobUrl());

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Android-Market/2 (sapphire PLAT-RC33); gzip");
            conn.setRequestProperty("Cookie", cookieName + "=" + cookieValue);

            InputStream inputstream =  (InputStream) conn.getInputStream();
            String fileToSave = assetId + ".apk";
            System.out.println("Downloading " + fileToSave);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileToSave));
            byte buf[] = new byte[1024];
            int k = 0;
            for(long l = 0L; (k = inputstream.read(buf)) != -1; l += k )
                stream.write(buf, 0, k);
            inputstream.close();
            stream.close();  
        } catch (Exception e) {
            System.err.println("Found exception");
            System.err.println(e);
            System.exit(1);
        }
    }
}
