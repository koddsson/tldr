(ns tldr.views.welcome
  (:require [tldr.views.common :as common]
            [monger.collection :as mc])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [monger.core :only [connect! connect set-db! get-db]]
        [monger.query])
  (:refer-clojure :exclude [sort find]))

(import [java.net URLEncoder])

(connect!)
(set-db! (get-db "tldr"))

(defn parseUrl [{:keys [_id url text date]}]
  "Parse a url into a HTML template to render it pretty"
   [:a {:href (URLEncoder/encode url) :class "list-group-item"}
    [:h4 {:class "list-group-item-heading"} text]
    [:span {:class "list-group-item-text"} (str "Date: " date " ")]
    [:span {:class "list-group-item-text pull-right"} url]])

(defn latestPartial []
  "Gets the 10 latest rows in the database"
  (with-collection "test"
    (find {})
    (sort (array-map :date -1))
    (limit 10)))

(defpage "/" []
  (common/layout
    [:h1 "tldr"]
    [:h3 "there is just too much internet"]
    [:form {:method "post" :role "form" }
      [:div {:class "form-group"}
        [:input {:name "url" :type "text" :class "form-control"
                 :placeholder "URL"}]]
      [:div {:class "form-group"}
        [:input {:name "text" :type "text" :class "form-control"
                 :placeholder "Text"}]]
      [:div {:class "form-group"}
        [:button {:type "submit" :class "btn btn-default"} "Submit"]]]
    (map parseUrl (latestPartial))))


(defpage [:post "/"] {:keys [text url]}
  (mc/insert "test" {:url url :text text :date (new java.util.Date)})
  (redirect (URLEncoder/encode url)))

(defpage "/:url" {url :url}
  (def results (seq (mc/find-maps "test" {:url url})))
  (common/layout
    [:a {:href url} [:h3 url]]
    [:div {:class "list-group"}
      (map parseUrl results)]
    [:form {:method "post" :role "form"}
      [:div {:class "form-group"}
        [:input {:name "text" :type "text" :class "form-control"
                 :placeholder "text"}]]
      [:div {:class "form-group"}
        [:button {:type "submit" :class "btn btn-default"} "Submit"]]]))

(defpage [:post "/:url"] {:keys [text] url :url}
  (mc/insert "test" { :url url :text text :date (new java.util.Date)})
  (redirect (URLEncoder/encode url)))
