(ns pub.routes.drink
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [pub.models.db :as db]
            [compojure.response :refer [render]]
            [buddy.auth :refer [authenticated?]]
            [liberator.core :refer [defresource]]
            [clojure.data.json :as json]
            [struct.core :as st]
            [clojure.java.io :as io]
            [liberator.representation :refer [ring-response as-response]]
            [clojure.set :refer [rename-keys]]
            [clojure.string :as str]
            [ring.util.response :refer [redirect]]))

(def drink-schema
  {
   :name  [st/required st/string]
   :price [st/required st/number]
   :drink_type  [st/required st/number]
   })

(defn drink-validation? [params]
  (println params)
  (st/valid?
   {:name  (:name params)
    :price (read-string (:price params))
    :drink_type  (read-string (:drink_type params))
    }
   drink-schema))


(defn get-add-drink-page [session & [message]]
  (if-not (authenticated? session)
    (redirect "/login")
    (render-file "templates/drink-add.html"
                 {:title   "Add drink"
                  :logged  (:identity session)
                  :message message
                  :types   (db/get-types)})))

(defn add-drink->db [params]
  (db/add-drink params))

(defn add-drink [{:keys [params session]}]
  (cond
    (not= (authenticated? session))
    (redirect "/login")
    (drink-validation? params)
    (redirect (str "/drink/" (:generated_key (add-drink->db params))))
    :else
    (->
     (get-add-drink-page session
                         {:text "All fields are required"
                          :type "error"}))))

(defn get-drink-page [page params session & [message]]
  (render-file page
               {:title   (str "drink " (:id params))
                :logged  (:identity session)
                :message message
                :drink   (first (db/find-drink params))
                :types   (db/get-types)}))

(defn get-drink [{:keys [params session]} & [message]]
  (cond
    (not (authenticated? session))
    (redirect "/login")
    (authenticated? session)
    (get-drink-page "templates/drink-admin.html" params session message)
    :else
    (redirect "/")))

(defn get-drinks [text]
  (if (or (nil? text)
          (= "" text))
    (db/get-drinks)
    (db/search-drinks text)))

(defn get-search-drinks [params session]
  (render-file "templates/drink-search.html"
               {:title  "Search drinks"
                :logged (:identity session)
                :drinks (get-drinks nil)}))

(defn update-drink-data [params]
  (db/update-drink params))

(defresource search-drinks [{:keys [params session]}]
  :allowed-methods [:post]
  :authenticated? (authenticated? session)
  :handle-created (json/write-str (get-drinks (:text params)))
  :available-media-types ["application/json"])

(defresource search-drinks [{:keys [params session]}]
  :allowed-methods [:get]
  :available-media-types ["text/html" "application/json"]
  :authorized? (fn [_] (authenticated? session))
  :handle-ok
  #(let [media-type (get-in % [:representation :media-type])]
    (condp = media-type
      "text/html"        (get-search-drinks params session)
      "application/json" (-> (:text params)
                             (get-drinks)
                             (json/write-str)))))

(defresource update-drink [{:keys [params session]}]
  :allowed-methods [:put]
  :available-media-types ["application/json"]
  :malformed? (fn [context] (not (drink-validation? params)))
  :handle-malformed "All fields are required"
  :exists? (fn [_] (not (empty? (db/find-drink (select-keys params [:id])))))
  :can-put-to-missing? false
  :authorized? (authenticated? session)
  :new? false
  :respond-with-entity? true
  :put! (fn [_] (update-drink-data params))
  :handle-ok
  (fn [_]
    (json/write-str
     {:message "drink successfully edited"
      :drink   (-> (select-keys params [:id])
                   (db/find-drink)
                   (first))}))
  :handle-not-implemented (fn [_] (str "There is no drink with id " (:id params))))

(defresource delete-drink [{:keys [params session]}]
  :allowed-methods [:delete]
  :malformed? (fn [_] (empty? (:id params)))
  :handle-malformed (fn [_] "Please provide an id")
  :exists? (fn [_] (not (empty? (db/find-drink params))))
  :handle-not-found (fn [_] (str "There is no drink with id " (:id params)))
  :authorized? (authenticated? session)
  :new? false
  :respond-with-entity? true
  :delete! (fn [_] (db/delete-drink (:id params)))
  :handle-ok (fn [_] (json/write-str "drink successfully deleted"))
  :available-media-types ["application/json"])

(defroutes drink-routes
  (GET "/drink" request (get-add-drink-page (:session request)))
  (POST "/drink" request (add-drink request))
  (PUT "/drink" request (update-drink request))
  (DELETE "/drink" request (delete-drink request))
  (GET "/drinks" request (search-drinks request))
  (GET "/drink/:id" request (get-drink request)))
