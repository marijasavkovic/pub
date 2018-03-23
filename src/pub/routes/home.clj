(ns pub.routes.home
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [compojure.response :refer [render]]
            [buddy.auth :refer [authenticated?]]
            [pub.models.db :as db]
            [ring.util.response :refer [redirect]]))

(defn get-home-page [page session]
  (render-file page
               {:title "Home"
                :logged (:identity session)
                :drinks (db/get-drinks)}))

(defn home [session]
  (cond
    ;(not (authenticated? session))
    ;(redirect "/login")
    (authenticated? session)
    (get-home-page "templates/home-admin.html" session)
    :else
    (get-home-page "templates/home.html" session)))

(defroutes home-routes
  (GET "/" request (home (:session request))))
