package com.example.cjnews.netClient;

import java.util.List;

public class Bean {

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public static class StoriesBean {

        private String image_hue;
        private String title;
        private String url;
        private String hint;
        private String ga_prefix;
        private int type;
        private int id;
        private List<String> images;

        public String getImage_hue() {
            return image_hue;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public String getHint() {
            return hint;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public List<String> getImages() {
            return images;
        }

    }

    public static class TopStoriesBean {

        private String image_hue;
        private String hint;
        private String url;
        private String image;
        private String title;
        private String ga_prefix;
        private int type;
        private int id;

        public String getImage_hue() {
            return image_hue;
        }

        public String getHint() {
            return hint;
        }

        public String getUrl() {
            return url;
        }

        public String getImage() {
            return image;
        }

        public String getTitle() {
            return title;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }
    }
}
