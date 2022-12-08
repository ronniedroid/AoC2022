(ns ronniedroid.dayfive
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input
  (slurp (io/resource "dayfiveinput.txt")))

(def crates
  (-> (str/replace input #"\[|\]" " ")
      (str/replace #" " "-")
      (str/split-lines)
      (as-> c
        (let [separator (.indexOf c "")
              c (take separator c)
              c (map #(str/split % #"") c)]
          (->>
           (apply map vector c)
           (mapv
            #(filterv
              (fn [x] (re-find #"[a-zA-Z0-9]" x))
              %))
           (filterv #(> (count %) 1))
           (map #(let [key (read-string (last %))
                       res (pop %)]
                   {key (apply list res)}))
           (apply merge))))))

(def steps
  (-> input
      (str/split-lines)
      (as-> c
        (let [separator (.indexOf c "")
              c (nthrest c (+ separator 1))
              c (map #(str/split % #" ") c)
              c (mapv #(filterv
                        (fn [x]
                          (re-find #"[0-9]" x))
                        %)
                      c)]
          (mapv #(mapv (fn [x] (read-string x)) %)
                c)))))

(defn crate-mover
  [model]
  (reduce
   (fn [acc step]
     (let [from (second step)
           to (last step)
           quantity (first step)]
       (->
         acc
         (assoc to
                (concat
                 (if (= model 9000)
                   (reverse (take quantity
                                  (get acc from)))
                   (take quantity (get acc from)))
                 (get acc to)))
         (assoc from
                (nthrest (get acc from)
                         quantity)))))
   crates
   steps))


(defn move-crates-with
  [func model]
  (->> (func model)
       (sort)
       (vals)
       (map #(first %))
       str/join))
