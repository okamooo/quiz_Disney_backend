-- クイズ関連データをまとめてリセット
-- 参照している子テーブルも含めて同時に空にする
TRUNCATE TABLE quiz_results, quiz_session_answers, choices, quizzes RESTART IDENTITY;

-- データ投入
INSERT INTO quizzes (category_id, phrase, translation, question_word, meaning, wrong_word1, wrong_word2, wrong_word3, explanation, created_at, updated_at) VALUES 
(1, 'I''ll make a man out of you.', '「お前を一人前にしてやる！」（ムーラン）', 'make', '作り変える', 'keep', 'find', 'take', '「make A (out of) B」で「BからAを作り出す（一人前にする）」という熟語です。', NOW(), NOW()),
(1, 'Test the limits and break through.', '「自分の限界に挑戦して、それをさらに越えていけ。」（アナと雪の女王）', 'limits', '限界', 'dreams', 'goals', 'starts', '「limit」は境界や制限。自分の能力の限界に挑戦するという意味です。', NOW(), NOW()),
(1, 'Not to be deceived by appearances, for beauty is found within.', '「外見に騙されてはいけない。美は内面に宿るのだから。」（美女と野獣）', 'deceived', '騙される', 'believed', 'helped', 'called', '「deceive（騙す）」の受身形。外見に惑わされてはいけないという教訓です。', NOW(), NOW()),
(1, 'I’m not a dumb bunny.', '「私はマヌケなウサギなんかじゃない！」（ズートピア）', 'dumb', 'まぬけな', 'smart', 'fast', 'cute', '本来は「口がきけない」ですが、口語では「馬鹿な」という強い意味で使われます。', NOW(), NOW()),
(1, 'We finish each other''s... sentences!', '「私たちは、お互いの…サンドイッチを！（言葉を言い当てる）」（アナと雪の女王）', 'sentences', '文・言葉', 'sandwiches', 'meals', 'dreams', '本来は「相手の言葉(sentences)を最後まで言う」という熟語を崩したジョークです。', NOW(), NOW()),
(1, 'And maybe it''s the party talking or the chocolate fondue.', '「もしかしたら、パーティーのノリでこんなこと言ってるだけかもしれないけど」（アナと雪の女王）', 'talking', '言わせている', 'dancing', 'singing', 'sleeping', '「無生物 + talking」で「〜のせいでそう言っているだけ」という表現です。', NOW(), NOW()),
(1, 'Isn''t it neat?', '「素敵じゃない？」（アリエル）', 'neat', '素晴らしい', 'dirty', 'small', 'hard', '「きちんとした」以外に、口語で「素敵」「かっこいい」という意味で使われます。', NOW(), NOW()),
(1, 'You want thingamabobs? I got twenty.', '「何か特別なものが欲しい？私は20個持っているわ」（アリエル）', 'twenty', '20個', 'twelve', 'thirty', 'two', 'アリエルが地上の道具（名前も知らない物）をたくさん持っていることを表しています。', NOW(), NOW()),
(1, 'Just wonder when will my life begin?', '「わたしの人生はいつになったら始まるのかしら？」（ラプンツェル）', 'wonder', '疑問に思う', 'know', 'forget', 'agree', '「〜かなあ」と期待を込めて自問自答する時に使う動詞です。', NOW(), NOW()),
(1, 'Always let your conscience be your guide.', '「いつもあなたの良心をあなたの道しるべにしなさい。」（ピノキオ）', 'conscience', '良心', 'science', 'confidence', 'convenience', '善悪を判断する内なる心の声。ジミニー・クリケットの重要な言葉です。', NOW(), NOW()),
(1, 'To infinity and beyond!', '「無限の彼方へ！」（トイ・ストーリー）', 'beyond', '〜を越えて', 'under', 'between', 'against', '「向こう側へ」という意味の前置詞。限界を決めないバズの決め台詞です。', NOW(), NOW()),
(1, 'I’m malicious, mean and scary.', '「オレはワルでケチでコワモテさ」（ラプンツェル）', 'malicious', '悪意のある', 'delicious', 'precious', 'suspicious', '他人を傷つけようとする意地の悪さを表す形容詞です。', NOW(), NOW()),
(1, 'Some people are worth melting for.', '「大切な人のためなら溶けてもいいよ。」（オラフ）', 'worth', '価値がある', 'safe', 'easy', 'free', '「be worth 〜ing」で「〜する価値がある」という重要構文です。', NOW(), NOW()),
(1, 'You must be true to your heart.', '「自分の心に素直になって。」（ムーラン）', 'true', '忠実な', 'false', 'kind', 'rude', '「自分に正直である」ことを「be true to oneself」と表現します。', NOW(), NOW()),
(1, 'There''s a snake in my boot!', '「俺のブーツにゃガラガラヘビ〜」（ウッディ）', 'boot', 'ブーツ', 'hat', 'bag', 'bed', 'ウッディの背中の紐を引いた時に流れるお決まりの台詞です。', NOW(), NOW()),
(1, 'All it takes is faith and trust.', '「必要なのは信じることだけ。」（ピーターパン）', 'faith', '信念', 'face', 'fate', 'fear', '目に見えないものを信じる強い心。ピーターパンの魔法の源です。', NOW(), NOW()),
(1, 'Reach for the sky!', '「手を上げろ！」（ウッディ）', 'reach', '届く・伸ばす', 'search', 'teach', 'watch', '「空を指せ＝手を上げろ」というカウボーイらしい言い回しです。', NOW(), NOW()),
(1, 'I’m so romantic, sometimes I think I should marry myself.', '「僕はすごくロマンティック。たまに、自分と結婚した方がいいって思うんだ。」（マイク）', 'marry', '結婚する', 'carry', 'merry', 'worry', 'マイクらしいユーモア溢れる自己愛の表現です。', NOW(), NOW()),
(1, 'When you use a bird to write with, it’s called tweeting.', '「鳥を使って何かを書くことは、ツイートと呼ばれているんだよ。」（マウイ）', 'tweeting', 'つぶやく', 'flying', 'eating', 'singing', '鳥がさえずることと、SNSの投稿を掛けたジョークです。', NOW(), NOW()),
(1, 'Now… bring me that horizon.', '「それじゃ・・・水平線まで連れてってくれ」（パイレーツ・オブ・カリビアン）', 'horizon', '水平線', 'hospital', 'holiday', 'history', '空と海が接する境界線。冒険への旅立ちを象徴する言葉です。', NOW(), NOW()),
(1, 'Wait. You got engaged to someone you just met?', '「ちょっとまって。君は会ったばかりの人と結婚したいのかい？」（クリストフ）', 'engaged', '婚約した', 'enraged', 'enjoyed', 'entered', '結婚を約束した状態。「be engaged to 〜」の形でよく使われます。', NOW(), NOW()),
(1, 'No! I don''t trust your judgement!', '「ダメだ！君の判断は信用できない。」（クリストフ）', 'judgement', '判断', 'agreement', 'treatment', 'movement', '「judge（判断する）」の名詞形。相手の決定や考えを指します。', NOW(), NOW()),
(1, 'Who''s afraid of the big bad wolf', '「狼なんか怖くない」（3びきの子豚）', 'afraid', '恐れて', 'angry', 'awake', 'alive', '「be afraid of 〜」で、何かを怖がっている状態を表します。', NOW(), NOW());