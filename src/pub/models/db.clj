(ns pub.models.db
  (:require [clojure.java.jdbc :as sql]
            [korma.core :as k]
            [korma.db :refer [defdb mysql]]
            [clj-time.coerce :as c]
            [clj-time.core :as t])
  (:import java.sql.DriverManager))

(def db-config (clojure.edn/read-string (slurp "conf/db-config.edn")))

(defdb db (mysql db-config))

(k/defentity user
  (k/table :user))

(k/defentity drink_type
  (k/table :drink_type))

(k/defentity drink
  (k/table :drink))

(k/defentity reservation
  (k/table :reservation))

(defn get-text-search [text]
  (str "%" text "%"))

(defn add-user [params]
  (k/insert user
  (k/values params)))

(defn find-user [params]
  (k/select user
            (k/where params)
            (k/order :username :ASC)))

(defn get-users []
  (k/select user
  (k/order :username :ASC)))

(defn update-user [params]
  (k/update user
            (k/set-fields params)
            (k/where {:id (:id params)})))

(defn add-drink [params]
  (k/insert drink
  (k/values params)))

(defn update-drink [params]
  (k/update drink
            (k/set-fields params)
            (k/where {:id (:id params)})))

(defn delete-drink [id]
  (k/delete drink
  (k/where {:id id})))

(defn get-drinks []
  (k/select drink
          (k/fields :* [:drink_type.name :tname])
          (k/join drink_type (= :drink_type :drink_type.id))
          (k/order :id :ASC)))

(defn search-drinks [text]
  (k/select drink
            (k/fields :* [:drink_type.name :tname])
            (k/join drink_type (= :drink_type :drink_type.id))
            (k/where (or
                       {:id text}
                       {:name [like (get-text-search text)]}
                       {:drink_type.name [like (get-text-search text)]}))
            (k/order :id :ASC)))

(defn find-drink [params]
  (k/select drink
            (k/fields :* [:drink_type.name :tname])
            (k/join drink_type (= :drink_type :drink_type.id))
            (k/where params)
            (k/order :id :ASC)))

(defn get-types []
  (k/select drink_type
            (k/order :id :ASC)))

(defn update-type [{:keys [id name]}]
  (k/update drink_type
          (k/set-fields {:name name})
          (k/where {:id id})))

(defn search-types [text]
  (k/select drink_type
            (k/where (or
                       {:id text}
                       {:name [like  (get-text-search text)]}))
            (k/order :id :ASC)))

(defn find-type [params]
  (k/select drink_type
            (k/where params)
            (k/order :id :ASC)))

(defn add-reservation [params]
  (k/insert reservation
  (k/values params)))

(defn delete-reservation [params]
  (k/delete reservation
  (k/where params)))

(defn get-reservation-count []
  (k/select reservation
          (k/fields :drink.name)
          (k/aggregate (count :*) :number)
          (k/group :drink)
          (k/join drink (= :drink :drink.id))))

(defn find-reservation [params]
  (k/select reservation
  (k/where params)))