package com;

interface Links {

    class HTML implements Links {
        private String pageUrl;
        private String result;

        public HTML(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        @Override
        public String toString() {
            return this.result = new FindDeadLinks(pageUrl).sortLinks();
        }
    }
}

