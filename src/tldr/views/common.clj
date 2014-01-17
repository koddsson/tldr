(ns tldr.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page :only [include-css html5]]))

(defn include-bootstrap [filename]
  "Includes bootstrap files using bootstrapcdn.com. Remember to include the
  type like so: js/bootstrap.min.js and css/bootstrap.min.css"
  (include-css (str "//netdna.bootstrapcdn.com/bootstrap/3.0.3/" filename)))

(defpartial layout [& content]
  (html5
    [:head
      [:title "tldr"]
      (include-css "/css/common.css")
      (include-bootstrap "css/bootstrap.min.css")
      (include-bootstrap "css/bootstrap-theme.min.css")
      (include-bootstrap "js/bootstrap.min.js")]
    [:body
      [:div#wrapper content]]))
