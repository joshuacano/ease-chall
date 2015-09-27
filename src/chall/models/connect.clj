(ns chall.models.connect 
  (use [clojure.string])
  (require [clojure.data.json :as json]))

(def hot-url "https://www.reddit.com/hot.json")
(def top-url "https://www.reddit.com/top.json")

(defn get-js [url] (json/read-str (slurp url) :key-fn keyword))

(defn get-data [x] (:children (:data x)))

(defn get-keys [row] (select-keys row [:title :subreddit_id :url :num_comments]))

(defn get-thumb-url [row] 
  "Get Thumbnail Url"
  (:thumbnail_url (:oembed (:media row))))

(defn get-display [row] 
  "Get final display for html template"
  (let [thumb (get-thumb-url row)]
    (assoc
      (get-keys row)
      :thumb (if (blank? thumb) (:thumbnail row) thumb))))

(defn get-all-rows [url] (map #(get-display (:data %)) (get-data (get-js url))))
