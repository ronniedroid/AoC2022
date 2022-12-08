(ns ronniedroid.daysix
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input
  (slurp (io/resource "daysixinput.txt")))

(defn find-start-of
  [coll subroutine]
  (let [n (if (= subroutine "packet") 4 14)]
    (->> coll
         (partition n 1)
         (map #(distinct %))
         (filter #(= (count %) n))
         first
         str/join)))

(defn find-marker-of
  [coll subroutine]
  (let [n (if (= subroutine "packet") 4 14)]
    (-> (str/replace-first
         coll
         (find-start-of coll subroutine)
         "-")
        (str/split #"")
        (.indexOf "-")
        (+ n)
        (take input)
        count)))
