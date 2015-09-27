(ns chall.models.dbstore
  (require [clojure.java.jdbc :as j]))

(def mysql-db  {:subprotocol "mysql"
                :subname "//localhost:3306/reddit"
                :user "reddit_app"
                :password "reddit"})

(defn update-password [cus_cd password]
  "Update password"
  (j/execute! mysql-db ["UPDATE customer SET password = ? WHERE cus_cd = ?" 
                        password cus_cd]))

(defn get-customer [cus_name password]
  (:cus_cd (first (j/query mysql-db ["SELECT cus_cd FROM customer WHERE name = ? and password = ?" cus_name password]))))

(defn has-customer [cus_name password]
  "Has Login"
  (not (nil? (get-customer cus_name password))))

(defn insert-customer [cus_name password]
  "Insert Customer"
  (do
    (j/execute! mysql-db ["INSERT INTO customer (name, password) VALUES (?, ?)" cus_name password] )
    (get-customer cus_name password)))

(defn insert-likes [cus_cd reddit_id url title num_comments img]
  "Store Document information for User on Reddit"
  (j/execute! mysql-db ["INSERT INTO customer_likes 
                        (cus_cd, reddit_id, url, title, num_comments, img) 
                        VALUES (?, ?, ?, ?, ?, ?)"
                        cus_cd reddit_id url title num_comments img]))  

(defn remove-likes [cus_cd reddit_id] 
  "Remove Doc from store"
  (j/execute! mysql-db [ "DELETE FROM customer_likes WHERE cus_cd = ? and reddit_id = ?"
                        cus_cd reddit_id]))

(defn select-likes [cus_cd]
  "Get Docs from store"
  (j/query mysql-db ["SELECT * FROM customer_likes WHERE cus_cd = ?" cus_cd]))

