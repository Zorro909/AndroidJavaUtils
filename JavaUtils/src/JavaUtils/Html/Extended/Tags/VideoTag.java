package JavaUtils.Html.Extended.Tags;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import JavaUtils.Html.CustomAttribute;
import JavaUtils.Html.HtmlAttribute;
import JavaUtils.Html.HtmlDocument;
import JavaUtils.Html.HtmlTag;
import JavaUtils.Parser.ParseObject;
import JavaUtils.RestAPI.Action;
import JavaUtils.RestAPI.RestAPIActionSet;
import JavaUtils.UtilHelpers.FileUtils;

public class VideoTag extends ExtendedTag {

    private boolean setUp = false;
    private HtmlTag src;
    private String css;
    private String js;

    public VideoTag(HtmlDocument doc, RestAPIActionSet raas, URI video, int width, int height)
                    throws URISyntaxException, IOException {
        super("video");
        if (!setUp) {
            ScriptTag st = new ScriptTag(null, FileUtils.readAll(this.getClass()
                            .getResourceAsStream("/JavaScriptSource/video-js.min.css")), raas);
            css = st.getScriptURL();
            st = new ScriptTag(null,
                            FileUtils.readAll(this.getClass()
                                            .getResourceAsStream("/JavaScriptSource/video.min.js")),
                            raas);
            js = st.getScriptURL();
            raas.addAction("/scripts/video.min.js.map", new Action() {
                
                @Override
                public boolean isRaw() {
                    return true;
                }
                
                @Override
                public ParseObject executeRequest(HashMap<String, String> conf, HashMap<String, String> vars) {
                    ParseObject po = new ParseObject("");
                    po.add("raw", "");
                    return po;
                }
            });
        }
        doc.body().addTag(new HtmlTag("link")
                        .addAttribute("href", new HtmlAttribute(css))
                        .addAttribute("rel", new HtmlAttribute("stylesheet")));
        doc.body().addTag(new HtmlTag("script").addAttribute("src",
                        new HtmlAttribute(js)));
        addAttribute("class", new HtmlAttribute("video-js vjs-default-skin"));
        addAttribute("controls", new HtmlAttribute(""));
        addAttribute("preload", new HtmlAttribute("auto"));
        addAttribute("width", new HtmlAttribute("" + width));
        addAttribute("height", new HtmlAttribute("" + height));
        addAttribute("data-setup", new HtmlAttribute("{}"));
        src = new HtmlTag("source");
        src.addAttribute("src", new CustomAttribute("video",video.toString()));
        addTag(src);
    }

    public void setVideoUrl(URI video) {
        src.addAttribute("src", new HtmlAttribute(video.toString()));
    }

}
