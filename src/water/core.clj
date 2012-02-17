(ns water.core)

(defrecord Position [x y])

(defrecord Node [value position north south east west drainsTo])

(defn make-position [x y] (Position. x y) )

(defn make-neighbor [x y relation maxX maxY] 
	(case relation
		:north (if (<= 0 (- y 1) ) (make-position x (- y 1)))
		:south (if (>= maxY (+ y 1) ) (make-position x (+ y 1)))
		:east (if (>= maxX (+ x 1) ) (make-position (+ x 1) y))
		:west (if (<= 0 (- x 1) ) (make-position (- x 1) y))
		nil)
)

(defn make-node [value x y maxX maxY] (Node. value (make-position x y) 
												   (make-neighbor x y :north maxX maxY) 
												   (make-neighbor x y :south maxX maxY) 
												   (make-neighbor x y :east maxX maxY)
												   (make-neighbor x y :west maxX maxY)
												   nil)
)

(defn make-nodes [drainage maxX maxY] (vec (for [y (range (+ maxY 1))] 
									  			(vec (for [x (range (+ 1 maxX))] (make-node (nth (nth drainage y) x) x y maxX maxY)))
									  		)))

(defn populate-drain [drainage] drainage)

(defn convert-map [drainage] (clojure.walk/prewalk-replace {1 "a" 2 "a"} drainage))