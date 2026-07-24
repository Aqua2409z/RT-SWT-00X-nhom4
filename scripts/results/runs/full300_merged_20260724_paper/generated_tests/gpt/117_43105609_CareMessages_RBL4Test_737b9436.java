
package com.riversoft.weixin.mp.care;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.common.message.Media;
import com.riversoft.weixin.common.message.News;
import com.riversoft.weixin.common.message.Text;
import com.riversoft.weixin.mp.base.AppSetting;
import com.riversoft.weixin.mp.care.bean.Card;
import com.riversoft.weixin.mp.care.bean.Music;
import com.riversoft.weixin.mp.care.bean.Video;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Map;

import static org.mockito.Mockito.*;

public class CareMessages_RBL4Test_737b9436 {

    private CareMessages careMessages;
    private WxClient wxClient;

    @Before
    public void setUp() {
        wxClient = mock(WxClient.class);
        careMessages = new CareMessages();
        careMessages.setWxClient(wxClient);
    }

    @Test
    public void testTextMessage() {
        String openId = "testOpenId";
        String text = "Hello World";
        careMessages.text(openId, text);

        verify(wxClient).post(anyString(), anyString());
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient).post(eq(WxEndpoint.get("url.care.message.send")), jsonCaptor.capture());

        String json = jsonCaptor.getValue();
        assert json.contains("\"msgtype\":\"text\"");
        assert json.contains("\"touser\":\"" + openId + "\"");
        assert json.contains("\"text\":{\"content\":\"" + text + "\"}");
    }

    @Test
    public void testImageMessage() {
        String openId = "testOpenId";
        String image = "image_url";
        careMessages.image(openId, image);

        verify(wxClient).post(anyString(), anyString());
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient).post(eq(WxEndpoint.get("url.care.message.send")), jsonCaptor.capture());

        String json = jsonCaptor.getValue();
        assert json.contains("\"msgtype\":\"image\"");
        assert json.contains("\"touser\":\"" + openId + "\"");
        assert json.contains("\"image\":{\"media_id\":\"" + image + "\"}");
    }

    @Test
    public void testVoiceMessage() {
        String openId = "testOpenId";
        String voice = "voice_url";
        careMessages.voice(openId, voice);

        verify(wxClient).post(anyString(), anyString());
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient).post(eq(WxEndpoint.get("url.care.message.send")), jsonCaptor.capture());

        String json = jsonCaptor.getValue();
        assert json.contains("\"msgtype\":\"voice\"");
        assert json.contains("\"touser\":\"" + openId + "\"");
        assert json.contains("\"voice\":{\"media_id\":\"" + voice + "\"}");
    }

    @Test
    public void testVideoMessage() {
        String openId = "testOpenId";
        Video video = new Video("video_id", "title", "description");
        careMessages.video(openId, video);

        verify(wxClient).post(anyString(), anyString());
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient).post(eq(WxEndpoint.get("url.care.message.send")), jsonCaptor.capture());

        String json = jsonCaptor.getValue();
        assert json.contains("\"msgtype\":\"video\"");
        assert json.contains("\"touser\":\"" + openId + "\"");
        assert json.contains("\"video\":{\"media_id\":\"video_id\",\"title\":\"title\",\"description\":\"description\"}");
    }

    @Test
    public void testMusicMessage() {
        String openId = "testOpenId";
        Music music = new Music("music_id", "title", "description", "music_url", "hq_music_url");
        careMessages.music(openId, music);

        verify(wxClient).post(anyString(), anyString());
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient).post(eq(WxEndpoint.get("url.care.message.send")), jsonCaptor.capture());

        String json = jsonCaptor.getValue();
        assert json.contains("\"msgtype\":\"music\"");
        assert json.contains("\"touser\":\"" + openId + "\"");
        assert json.contains("\"music\":{\"media_id\":\"music_id\",\"title\":\"title\",\"description\":\"description\",\"musicurl\":\"music_url\",\"hqmusicurl\":\"hq_music_url\"}");
    }

    @Test
    public void testNewsMessage() {
        String openId = "testOpenId";
        News news = new News("title", "description", "url", "picurl");
        careMessages.news(openId, news);

        verify(wxClient).post(anyString(), anyString());
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient).post(eq(WxEndpoint.get("url.care.message.send")), jsonCaptor.capture());

        String json = jsonCaptor.getValue();
        assert json.contains("\"msgtype\":\"news\"");
        assert json.contains("\"touser\":\"" + openId + "\"");
        assert json.contains("\"news\":{\"articles\":[{\"title\":\"title\",\"description\":\"description\",\"url\":\"url\",\"picurl\":\"picurl\"}]}");
    }

    @Test
    public void testCardMessage() {
        String openId = "testOpenId";
        String cardId = "card_id";
        careMessages.card(openId, cardId);

        verify(wxClient).post(anyString(), anyString());
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(wxClient).post(eq(WxEndpoint.get("url.care.message.send")), jsonCaptor.capture());

        String json = jsonCaptor.getValue();
        assert json.contains("\"msgtype\":\"wxcard\"");
        assert json.contains("\"touser\":\"" + openId + "\"");
        assert json.contains("\"wxcard\":{\"card_id\":\"" + cardId + "\"}");
    }
}
