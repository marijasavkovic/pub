(ns pub.routes.type
  (:require [compojure.core :refer :all]
            [buddy.auth :refer [authenticated?]]
            [selmer.parser :refer [render-file]]
            [buddy.auth :refer [authenticated?]]
            [pub.models.db :as db]
            [liberator.core :refer [defresource]]
            [clojure.data.json :as json]
            [struct.core :as st]
            [clojure.set :refer [rename-keys]]
            [ring.util.response :refer [redirect]]))

(def type-schema
  {:id [st/required st/number]
   :name [st/required st/string]})

(defn type-validation? [params]
  (st/valid? {:id (read-string (:id params))
              :name (:name params)} type-schema))

(defn get-types [text]
  (if (or (nil? text)
          (= "" text))
    (db/get-types)
    (db/search-types text)))

(defn get-type [page-name params session]
  (render-file page-name {:title "type"
                          :logged (:identity session)
                          :type (first (db/find-type (select-keys params [:id])))
                          :drinks (db/find-drink (rename-keys params {:id :drink_type}))}))

(defn get-type-page [{:keys [params session]}]
  (cond
    (not (authenticated? session))
    (redirect "/login")
    (authenticated? session)
    (get-type "templates/type-admin.html" params session)
    :else
    (redirect "/")))

(defresource search-types [{:keys [params session]}]
  :allowed-methods [:get]
  :available-media-types ["text/html" "application/json"]
  :authorized? (fn [_] (authenticated? session))
  :handle-ok #(let [media-type (get-in % [:representation :media-type])]
                    (condp = media-type
                      "text/html" (render-file "templates/type-search.html"
                                               {:title "Search types"
                                                :logged (:identity session)
                                                :types (get-types nil)})
                      "application/json" (->(:text params)
                                            (get-types)
                                            (json/write-str)))))

(defresource update-type [{:keys [params session]}]
  :allowed-methods [:put]
  :available-media-types ["application/json"]
  :malformed? (fn [_] (not (type-validation? params)))
  :handle-malformed "Please provide id and name"
  :exists? (fn [_] (not (empty? (db/find-type (select-keys params [:id])))))
  :can-put-to-missing? false
  :new? false
  :respond-with-entity? true
  :authorized? (fn [_] (authenticated? session))
  :put! (db/update-type params)
  :handle-ok (json/write-str "type successfully edited")
  :handle-not-implemented (fn [_] (str "There is no type with id " (:id params))))

(defroutes type-routes
  (GET "/types" request (search-types request))
  (GET "/type/:id" request (get-type-page request))
  (PUT "/type" request (update-type request)))
