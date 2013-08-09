(ns icfp2013.util)

(defn hex
  "Returns the number x as hex string."
  [x]
  (str "0x" (format "%016X" x)))

