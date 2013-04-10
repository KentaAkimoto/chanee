(ns chanee.core
  (:import java.io.File)
  (:gen-class))


(defn rename [f idx exec?]
  "rename one file"
  (println (str (. f getParent) "/" (.. f getName) " => " (. f getParent) "/" (format "%1$05d" idx) "_" (.. f getName)))
  (if (= true exec?) (. f renameTo (File. (str (. f getParent) "/" (format "%1$05d" idx) "_" (.. f getName))))) ;; リネーム処理
  )

(defn rename-ls [d exec?]
  "Usage: (rename-ls (File. \".\")"
  (println "Files in " (.getName d)) 
  (def idx (ref 110)) ;; ファイル名に付けるインデックス番号の初期値
  (println @idx)
   (doseq [f (sort (.listFiles d))]
   (if (.isDirectory f)
     (print "d ")
     (print "- "))
   (chanee.core/rename f @idx exec?)
   (dosync (ref-set idx (- @idx 2))))) ;; ファイル名に付けるインデックス番号の増分を指定

(defn -main
  "entry point"
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (print "type target path => ")
  (flush)
  (def line (read-line))
  (print (str "target path => " line))
  (newline)

  (chanee.core/rename-ls (File. line) false) ;; 処理対象フォルダパス

  (print "実行して良いですか？[y/n] => ")
  (flush)
  (def ok? (read-line))
  (print (str "your typed => " ok?))
  (newline)
  
  (if (= "y" ok?) (chanee.core/rename-ls (File. line) true))
  )
