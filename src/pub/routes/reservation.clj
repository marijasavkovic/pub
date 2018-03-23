(ns pub.routes.reservation
  (:require [compojure.core :refer :all]
            [pub.models.db :as db]
            [buddy.auth :refer [authenticated?]]
            [selmer.parser :refer [render-file]]
            [liberator.core :refer [defresource]]
            [clojure.data.json :as json]
            [struct.core :as st]
            [ring.util.response :refer [redirect]]))

(def reservation-schema
  {
   :person  [st/required st/string]
   :person_number [st/required st/number]
   :reservation_date  [st/required st/string]
   })

(defn reservation-validation? [params]
  (println params)
  (st/valid?
    {:person  (:person params)
     :person_number (read-string (:person_number params))
     :reservation_date (:reservation_date params)
     }
    reservation-schema))

(defn get-reservation-page [{:keys [params session]} & [message]]
  (if (nil? (:id params) )
    (render-file "templates/reservation.html"
                 {:title   "Reservation"
                  :view false
                  :message message})
    (render-file "templates/reservation.html"
                 {:title   "Reservation"
                  :message "Reservation successful"
                  :view true
                  :reservation (first (db/find-reservation params))})))

(defn handle-reservation [{:keys [params session]}]
  (cond
    (reservation-validation? params)
    (redirect (str "/reservation?id=" (:generated_key (db/add-reservation params))))
    :else
    (->
      (get-reservation-page session
                          {:text "All fields are required"
                           :type "error"}))))

(defroutes reservation-routes
  (POST "/reservation" request (handle-reservation request))
  (GET "/reservation" request (get-reservation-page request)))
