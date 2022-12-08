(ns ronniedroid.daysix
  (:gen-class)
  (:require [clojure.string :as str]))

(def input (slurp "resources/daysixinput.txt"))

(defn find-marker
  [string n]
  (->> string
       (str/trim-newline)
       (partition n 1)
       (map #(distinct %))
       (map #(count %))
       (take-while (complement #{n}))
       count
       (+ n)))

;; first try

;; (defn find-start-of
;;   [coll subroutine]
;;   (let [n (if (= subroutine "packet") 4 14)]
;;     (->> coll
;;          (partition n 1)
;;          (map #(distinct %))
;;          (filter #(= (count %) n))
;;          first
;;          str/join)))

;; (defn find-marker-of
;;   [coll subroutine]
;;   (let [n (if (= subroutine "packet") 4 14)]
;;     (-> (str/replace-first
;;          coll
;;          (find-start-of coll subroutine)
;;          "-")
;;         (str/split #"")
;;         (.indexOf "-")
;;         (+ n)
;;         (take input)
;;         count)))
