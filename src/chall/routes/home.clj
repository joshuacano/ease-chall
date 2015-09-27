(ns chall.routes.home
  (:require [chall.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok found ]]
            [chall.models.connect :as cn]
            [chall.models.dbstore :as db]
            [taoensso.timbre :as timbre]
            [clojure.java.io :as io]))

(defn set-user-redirect [cus_cd req]
  "Set customer in session and rerout to HOT url"
  (let [resp (found "/hot")]
    (assoc resp :session (assoc (:session req) :cus_cd cus_cd))))

(defn add-thumb [seqnce]
  "Add thumb img for DB rows"
  (map #(assoc % :thumb (:img %)) seqnce))

(defn get-login-request [req] 
  "Parse login from passed in map"
  (let [username (:username (:params req)) 
        password (:password (:params req))]
    {:username username :password password}))

(defn process-login-request [username password] 
  "Insert/select login from db"
  (if (db/has-customer username password)
    (db/get-customer username password))
  (db/insert-customer username password))

(defn get-customer-from-session [req]
  "Retrive customer from session"
  (:cus_cd (:session req)))

(defn process-and-redirect [req]
  "Login procedure for app"
  (as-> req $
    (get-login-request $)
    (process-login-request (:username $) (:password $))
    (set-user-redirect $ req)))

(defn verify-login [req func & args]
  "Verify that user is currently logged in"
  (let [cus (get-customer-from-session req)]
    (cond 
      (nil? cus) (found "/")
      :else (if args (apply func args) (func)))))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn hot-page []
  (layout/render "hot.html" {:rows (cn/get-all-rows cn/hot-url)}))

(defn top-page []
  (layout/render "top.html" {:rows (cn/get-all-rows cn/top-url)}))

(defn my-likes-page [cus_cd]
  (layout/render "my-likes.html" {:rows (add-thumb (db/select-likes cus_cd))}))

(defroutes home-routes
  (GET "/" [] (home-page))

  (GET "/post-customer" request 
       (process-and-redirect request))

  (POST "/post-likes" request
        (let [{:keys [subreddit_id url title num_comments img]} (:params request)
              cus_cd (get-customer-from-session request)]
          (db/insert-likes cus_cd subreddit_id url title num_comments img)
          {:body (str "inserted " cus_cd " " url " " subreddit_id " " title " " num_comments " " img)}))

  (GET "/hot" request
       (verify-login request hot-page))

  (GET "/top" request 
       (verify-login request top-page ))

  (GET "/my-likes" request
       (let [cus_cd (get-customer-from-session request)]
         (verify-login request my-likes-page cus_cd)))
  )



