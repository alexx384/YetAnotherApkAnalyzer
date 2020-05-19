import csv
import numpy as np
from sklearn.base import ClassifierMixin
from sklearn.feature_selection import chi2
from sklearn.feature_selection import mutual_info_classif
# Machine learning algorithms
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix
from sklearn.metrics import classification_report
from sklearn.metrics import roc_curve
from sklearn.metrics import auc
from sklearn.preprocessing import minmax_scale
from keras.wrappers.scikit_learn import KerasClassifier
from sklearn import model_selection
from sklearn import metrics
from sklearn.model_selection import LeaveOneOut
from sklearn import svm
from sklearn.feature_selection import SelectKBest
from sklearn.neural_network import MLPClassifier
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import Embedding
import time
# Graphics
import matplotlib.pyplot as plt
# Type checking, see PEP 484
import typing


DATASET_CSV = "newResult.csv"
ANSWERS_CSV = "newAnswer.csv"
PROPERTY_NAMES_CSV = "properties.csv"
SELECTED_FEATURES_CSV = "selected.csv"
MALWARE_SIGN = 1
LEGITIMATE_SIGN = 0


def drawMean(dataset: np.ndarray, answers: np.ndarray):
    # Calculate mean value for every predictor
    features_num = dataset[0].__len__()
    malw_arr = np.zeros(features_num, dtype=np.int64)
    malw_sum_num = 0
    legitimate_arr = np.zeros(features_num, dtype=np.int64)
    legitimate_sum_num = 0

    for i in range(len(dataset)):
        if answers[i] == MALWARE_SIGN:
            malw_arr += dataset[i]
            malw_sum_num += 1
        elif answers[i] == LEGITIMATE_SIGN:
            legitimate_arr += dataset[i]
            legitimate_sum_num += 1

    malw_arr = malw_arr / malw_sum_num
    legitimate_arr = legitimate_arr / legitimate_sum_num

    # Remove very high values
    # items_to_remove = {3, 32, 74}
    # items_to_remove: set = set()
    # items_to_remove = {4, 52, 101}
    items_to_remove: set = set()
    for i in range(features_num):
        if malw_arr[i] > 1000 and malw_arr[i] not in items_to_remove:
            items_to_remove.add(i)

    for i in range(features_num):
        if legitimate_arr[i] > 1000 and legitimate_arr[i] not in items_to_remove:
            items_to_remove.add(i)

    tmp_mal = np.empty(features_num - items_to_remove.__len__())
    tmp_legitim = np.empty(features_num - items_to_remove.__len__())

    print("Remove " + str(items_to_remove.__len__()) + " items")

    start_factor = 0
    for i in range(0, features_num):
        if malw_arr[i] > 1000 or legitimate_arr[i] > 1000:
            start_factor += 1
            continue

        tmp_mal[i - start_factor] = malw_arr[i]
        tmp_legitim[i - start_factor] = legitimate_arr[i]

    malw_arr = tmp_mal
    legitimate_arr = tmp_legitim

    # Draw graphics
    plt.plot(malw_arr, 'r', label="Malware")
    plt.plot(legitimate_arr, 'g-.', label="Legitimate")
    plt.xlabel("Classification number")
    plt.ylabel('Mean classification values')
    plt.legend(loc='best')
    plt.show()


def ROC(predicted, y_test):
    FPr, TPr, _threshold = roc_curve(y_test, predicted)
    roc_auc = auc(FPr, TPr)
    lw = 2

    plt.figure()
    plt.plot(
        FPr, TPr, lw=lw, label='Logistic Regression (area = %0.2f)' % roc_auc
    )
    plt.plot([0, 1], [0, 1], 'r--')
    plt.xlim([0.0, 1.0])
    plt.ylim([0.0, 1.05])
    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.title('Receiver operating characteristic')
    plt.legend(loc="lower right")
    plt.savefig('Log_ROC')
    plt.show()


def cross_validation(block_num: int, dataset: np.ndarray, answers: np.ndarray, model: ClassifierMixin, cross_validation: ):
    def tp(y_true, y_pred): return confusion_matrix(y_true, y_pred)[1, 1]
    def tn(y_true, y_pred): return confusion_matrix(y_true, y_pred)[0, 0]
    def fp(y_true, y_pred): return confusion_matrix(y_true, y_pred)[0, 1]
    def fn(y_true, y_pred): return confusion_matrix(y_true, y_pred)[1, 0]
    scoring = {
        'tp': metrics.make_scorer(tp),
        'tn': metrics.make_scorer(tn),
        'fp': metrics.make_scorer(fp),
        'fn': metrics.make_scorer(fn),
        'accuracy': metrics.make_scorer(metrics.accuracy_score),
        'precision': metrics.make_scorer(metrics.precision_score),
        'recall': metrics.make_scorer(metrics.recall_score),
        'f1_score': metrics.make_scorer(metrics.f1_score)
    }
    kFold = model_selection.KFold(n_splits=block_num, random_state=None, shuffle=False)
    results = model_selection.cross_validate(estimator=model, X=dataset, y=answers, cv=kFold, scoring=scoring)

    print('=== Cross validation [' + str(block_num) + '] ===')
    print('TP rate:   ' + str(results['tp']))
    print('TN rate:   ' + str(results['tn']))
    print('FP rate:   ' + str(results['fp']))
    print('FN rate:   ' + str(results['fn']))
    print('Accuracy:  ' + str(results['accuracy']))
    print('Precision: ' + str(results['precision']))
    print('Recall:    ' + str(results['recall']))
    print('F1_score:  ' + str(results['f1_score']))


def RandomSampling(X, Y):
    tn = 0
    fp = 0
    fn = 0
    fp = 0
    education_time = 0.0
    test_time = 0.0

    # clf = svm.SVC(gamma='auto', random_state=0)
    # clf = RandomForestClassifier(n_jobs=2, random_state=0)
    clf = MLPClassifier(hidden_layer_sizes=(140, 90, 60, 40), random_state=0)
    X_train, X_test, y_train, y_test = train_test_split(X, Y)

    start_time = time.time()
    clf.fit(X_train, y_train)
    education_time = time.time() - start_time

    start_time = time.time()
    proba = clf.predict(X_test)
    test_time = time.time() - start_time

    tn, fp, fn, tp = confusion_matrix(y_test, proba, labels=[0, 1]).ravel()

    summ = tn + fp + fn + tp
    print("\n\n\n")

    print(tp / summ)
    print(tn / summ)
    print(fp / summ)
    print(fn / summ)

    print(tn / (tn + fn))  # Precision
    print(tn / (tn + tp))  # Recall

    print(metrics.f1_score(y_test, proba, average='binary'))
    print(education_time)
    print(test_time)

    print("\n\n\n")


def itemWiseSampling(X, Y):
    TN = 0
    FP = 0
    FN = 0
    TP = 0
    F1 = 0
    Educate = 0.0
    Test = 0.0
    count = 0

    loo = LeaveOneOut()
    loo.get_n_splits(X)

    # clf = svm.SVC(gamma='auto', random_state=0)
    # clf = RandomForestClassifier(n_jobs=2, random_state=0)
    clf = MLPClassifier(hidden_layer_sizes=(140, 90, 60, 40), random_state=0)

    print(loo.split(X))

    for train_index, test_index in loo.split(X):

        start_time = time.time()
        clf.fit(X[train_index], Y[train_index])
        education_time = time.time() - start_time

        start_time = time.time()
        proba = clf.predict(X[test_index])
        test_time = time.time() - start_time

        Educate += education_time
        Test += test_time

        tn, fp, fn, tp = confusion_matrix(
            Y[test_index], proba, labels=[0, 1]
        ).ravel()
        TN += tn
        FP += fp
        FN += fn
        TP += tp

        # print(count)
        count += 1
        F1 += (metrics.f1_score(Y[test_index], proba, average='binary'))

    summ = TP + TN + FP + FN

    print("\n\n\n")
    print(TP / summ)
    print(TN / summ)
    print(FP / summ)
    print(FN / summ)

    print(TN / (TN + FN))  # Precision
    print(TN / (TN + TP))  # Recall

    print(F1 / len(Y))  # F1 score ???
    print(Educate)  # Educate time
    print(Test)  # Test time

    print("\n\n\n")


# def create_deep_model(optimizer='rmsprop', init='glorot_uniform'):
#     # create model
#     model = Sequential()
#     model.add

#     model.add(Dense(12, input_dim=8, kernel_initializer=init, activation='relu'))
#     model.add(Dense(8, kernel_initializer=init, activation='relu'))
#     model.add(Dense(1, kernel_initializer=init, activation='sigmoid'))
#     # Compile model
#     model.compile(loss='binary_crossentropy', optimizer=optimizer, metrics=['accuracy'])
#     return model


def convolutional_networks():
    model = Sequential()

    model.add(Embedding())

    model.add(Dense(150, input_dim=300, activation='sigmoid'))
    model.add(Dense(150, input_dim=300, activation='sigmoid'))
    model.add(Dense(150, input_dim=300, activation='sigmoid'))
    model.add(Dense(150, activation='sigmoid'))

    model.add(Dense(1, activation='sigmoid'))

    model.compile(loss='binary_crossentropy',
                  optimizer='rmsprop',
                  metrics=['binary_accuracy'])
    return model


def main():
    dataset = np.genfromtxt(DATASET_CSV, dtype=np.int32, delimiter=',')
    answers = np.genfromtxt(ANSWERS_CSV, dtype=np.int32, delimiter=',')
    property_names = np.genfromtxt(PROPERTY_NAMES_CSV, dtype=np.str, delimiter=',')
    # drawMean(dataset, answers)

    selecter = SelectKBest(score_func=chi2, k=80)
    selecter.fit(dataset, answers)
    property_choosen: np.ndarray = selecter.get_support()
    selecter.transform(dataset)

    with open('choosen_properties.csv', 'w') as file:
        i = 0
        for i in range(property_choosen.size):
            if property_choosen[i]:
                file.write(property_names[i])
                file.write(',')

    # Reduce dataset
    # dataset = optimize_parameters(dataset, answers, mutual_info_classif)
    # dataset = optimize_parameters(dataset, answers, chi2)
    # drawMean(dataset, answers)

    x_train0, x_test0, y_train0, y_test0 = train_test_split(
        dataset, answers, test_size=0.25, random_state=3)

    x_train, x_test, y_train, y_test = train_test_split(
        dataset, answers, test_size=0.25, random_state=0)

    # scale вместо auto, если будут ошибки
    # clf = svm.SVC(gamma='auto', random_state=0)
    # clf = RandomForestClassifier(n_jobs=2, random_state=0)
    clf = MLPClassifier(hidden_layer_sizes=(140, 90, 60, 40), random_state=0)
    clf.fit(x_train0, y_train0)
    clf.fit(x_train, y_train)

    predicted = clf.predict(x_test)
    print('Confusion Matrix : \n', confusion_matrix(y_test, predicted))

    report = classification_report(
        y_test, predicted, target_names=['Non malicious', 'Malicious']
    )

    print(report)

    ROC(predicted, y_test)

    buffer_test = minmax_scale(dataset, feature_range=(0, 1), axis=0)
    nptraining = np.array(buffer_test, "float32")
    nptarget = np.array(answers, "float32")

    print("[ + ] Method K-Nearest Neighbor\n")
    cross_validation(5, nptraining, nptarget, clf)
    cross_validation(10, nptraining, nptarget, clf)
    cross_validation(20, nptraining, nptarget, clf)
    RandomSampling(nptraining, nptarget)
    itemWiseSampling(nptraining, nptarget)

    print("Done")


if __name__ == "__main__":
    main()
