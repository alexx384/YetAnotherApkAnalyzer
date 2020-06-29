import numpy as np
from sklearn import neural_network
from sklearn import base
from sklearn import feature_selection
from sklearn import model_selection
from sklearn import metrics
from sklearn import preprocessing
from sklearn import svm
from sklearn import ensemble
import matplotlib.pyplot as plt
import os


STORAGE_PATH = '/content/drive/My Drive/diplom/'
MALWARE_TYPE = 'adware'

MALWARE_RESULT_CSV = '{}{}Result.csv'.format(STORAGE_PATH, MALWARE_TYPE)
MALWARE_ANSWER_CSV = '{}{}Answer.csv'.format(STORAGE_PATH, MALWARE_TYPE)
BENIGN_RESULT_CSV = '{}benignResult.csv'.format(STORAGE_PATH)
BENIGN_ANSWER_CSV = '{}benignAnswer.csv'.format(STORAGE_PATH)
LOG_FILENAME = '{}log/{}BenignLog.'.format(STORAGE_PATH, MALWARE_TYPE)
RESULT_FILENAME = '{}result/{}BenignChi2Result.'.format(STORAGE_PATH, MALWARE_TYPE)


def cross_validation(dataset: np.ndarray, answers: np.ndarray, model: base.ClassifierMixin,
                     cross_validator: model_selection.BaseCrossValidator, save_worst_data: bool) -> float:
    iteration_counter: int = 0
    f1_score_value = 0
    worst_f1_score_value = 1.0
    worst_predicted = None
    worst_actual = None

    for train_index, test_index in cross_validator.split(dataset, answers):
        train_x, test_x = dataset[train_index], dataset[test_index]
        train_y, test_y = answers[train_index], answers[test_index]
        iteration_counter += 1

        # Train
        model.fit(train_x, train_y)

        # Test
        predicted = model.predict(test_x)

        # Evaluate
        f1_iteration_score_value = metrics.f1_score(test_y, predicted, average='weighted')
        if f1_iteration_score_value <= worst_f1_score_value:
            worst_f1_score_value = f1_iteration_score_value
            worst_predicted = predicted
            worst_actual = test_y

        f1_score_value += f1_iteration_score_value

    if save_worst_data:
        np.savetxt(RESULT_FILENAME + 'predicted.txt', worst_predicted)
        np.savetxt(RESULT_FILENAME + 'actual.txt', worst_actual)

    return f1_score_value / iteration_counter


def kfold_cv(block_num: int, dataset: np.ndarray, answers: np.ndarray,
             model: base.ClassifierMixin, save_worst_data: bool = False) -> float:
    k_fold = model_selection.StratifiedKFold(n_splits=block_num, shuffle=True, random_state=0)
    return cross_validation(dataset, answers, model, k_fold, save_worst_data)


def random_sampling_cv(dataset: np.ndarray, answers: np.ndarray, model: base.ClassifierMixin) -> float:
    x_train, x_test, y_train, y_test = model_selection.train_test_split(dataset, answers, shuffle=True,
                                                                        stratify=answers)
    model.fit(x_train, y_train)
    prediction = model.predict(x_test)

    f1_score = metrics.f1_score(y_test, prediction, average='weighted')
    return f1_score


def get_score(dataset: np.array, answers: np.array, parametrs: int, model: base.ClassifierMixin, score_func)\
        -> (float, float, float, float, float):
    selecter = feature_selection.SelectKBest(score_func=score_func, k=parametrs)
    selecter.fit(dataset, answers)
    transformed_dataset = selecter.transform(dataset)
    x_train, x_test, y_train, y_test = model_selection.train_test_split(
        transformed_dataset, answers, random_state=0, stratify=answers)

    model.fit(x_train, y_train)
    prediction = model.predict(x_test)
    simple_score = metrics.f1_score(y_test, prediction, average='weighted')

    buffer_test = preprocessing.minmax_scale(transformed_dataset, feature_range=(0, 1), axis=0)
    nptraining = np.array(buffer_test, 'float32')
    nptarget = np.array(answers, 'float32')
    print('sample_score is done')
    k5_score = kfold_cv(5, nptraining, nptarget, model)
    print('k5_score is done')
    k10_score = kfold_cv(10, nptraining, nptarget, model)
    print('k10_score is done')
    k20_score = kfold_cv(20, nptraining, nptarget, model)
    print('k20_score is done')
    random_score = random_sampling_cv(nptraining, nptarget, model)
    return simple_score, k5_score, k10_score, k20_score, random_score


def save_best_score_report(properties_num: list, dataset: np.array, answers: np.array,
                           model: base.ClassifierMixin, log_filename: str, score_func):
    best_sample_score = 0.0
    best_k5_score = 0.0
    best_k10_score = 0.0
    best_k20_score = 0.0
    best_random_score = 0.0
    best_sample_properties = properties_num[0]
    best_k5_properties = properties_num[0]
    best_k10_properties = properties_num[0]
    best_k20_properties = properties_num[0]
    best_random_properties = properties_num[0]

    with open(log_filename, 'w') as file:
        for i in properties_num:
            file.write('properties num: ' + str(i) + '\n')
            sample_score, k5_score, k10_score, k20_score, random_score = get_score(
                dataset, answers, i, model, score_func)
            file.write('sample_score: ' + str(sample_score) + '\n')
            file.write('k5_score: ' + str(k5_score) + '\n')
            file.write('k10_score: ' + str(k10_score) + '\n')
            file.write('k20_score: ' + str(k20_score) + '\n')
            file.write('random_score: ' + str(random_score) + '\n')
            file.flush()
            print(str(i) + ' is done')

            if best_sample_score < sample_score:
                best_sample_score = sample_score
                best_sample_properties = i
            if best_k5_score < k5_score:
                best_k5_score = k5_score
                best_k5_properties = i
            if best_k10_score < k10_score:
                best_k10_score = k10_score
                best_k10_properties = i
            if best_k20_score < k20_score:
                best_k20_score = k20_score
                best_k20_properties = i
            if best_random_score < random_score:
                best_random_score = random_score
                best_random_properties = i

        file.write('The best values\n')
        file.write('sample_score: ' + str(best_sample_score) + ', properties = ' + str(best_sample_properties) + '\n')
        file.write('k5_score: ' + str(best_k5_score) + ', properties = ' + str(best_k5_properties) + '\n')
        file.write('k10_score: ' + str(best_k10_score) + ', properties = ' + str(best_k10_properties) + '\n')
        file.write('k20_score: ' + str(best_k20_score) + ', properties = ' + str(best_k20_properties) + '\n')
        file.write('random_score: ' + str(best_random_score) + ', properties = ' + str(best_random_properties) + '\n')


def main1():
    malware_result = np.genfromtxt(MALWARE_RESULT_CSV, dtype=np.int32, delimiter=',')
    malware_answer = np.genfromtxt(MALWARE_ANSWER_CSV, dtype=np.int32, delimiter=',')
    benign_result = np.genfromtxt(BENIGN_RESULT_CSV, dtype=np.int32, delimiter=',')
    benign_answer = np.genfromtxt(BENIGN_ANSWER_CSV, dtype=np.int32, delimiter=',')
    total_result = np.concatenate([malware_result, benign_result])
    total_answer = np.concatenate([malware_answer, benign_answer])

    dataset = total_result
    answers = total_answer
    model = svm.SVC(gamma=0.001, C=1, kernel='rbf', random_state=0)

    selecter = feature_selection.SelectKBest(score_func=feature_selection.chi2, k=50)
    selecter.fit(dataset, answers)
    transformed_dataset = selecter.transform(dataset)

    buffer_test = preprocessing.minmax_scale(transformed_dataset, feature_range=(0, 1), axis=0)
    nptraining = np.array(buffer_test, 'float32')
    nptarget = np.array(answers, 'float32')

    k5_score = kfold_cv(5, nptraining, nptarget, model, True)
    print(k5_score)

    model = svm.SVC(gamma=0.001, C=1, kernel='rbf', random_state=0)
    save_best_score_report([50, 125, 250, 500, 750, 1000, 1500], total_result, total_answer, model,
                           LOG_FILENAME + 'chi2.SVM.txt',
                           feature_selection.chi2)
    model = ensemble.RandomForestClassifier(n_estimators=600, max_depth=8, random_state=0)
    save_best_score_report([50, 125, 250, 500, 750, 1000, 1500], total_result, total_answer, model,
                           LOG_FILENAME + 'chi2.RF.txt',
                           feature_selection.chi2)
    model = neural_network.MLPClassifier(hidden_layer_sizes=(320, 160, 80, 40, 20, 10), random_state=0)
    save_best_score_report([750], total_result, total_answer, model, LOG_FILENAME + 'chi2.DP751.txt',
                           feature_selection.chi2)


def set_values(result: dict, files: list, root: str):
    for key in result:
        predicted = (key + '.predicted.txt')
        actual = (key + '.actual.txt')
        if predicted in files and actual in files:
            predicted_value = np.loadtxt(root + '/' + predicted)
            actual_value = np.loadtxt(root + '/' + actual)
            fpr, tpr, _ = metrics.roc_curve(actual_value, predicted_value)
            roc_auc = metrics.auc(fpr, tpr)
            result[key] = (fpr, tpr, roc_auc)
        else:
            print('The value does not assigned: {}'.format(key))
            return False

    return True


def get_roc_values_by_classifier_selector(selector: str, classifier: str) -> None or dict:
    result = {
            ('adwareBenign.k20.' + selector + '.' + classifier): None,
            ('scarewareBenign.k20.' + selector + '.' + classifier): None,
            ('ransomwareBenign.k20.' + selector + '.' + classifier): None,
            ('androidBotnetsBenign.k20.' + selector + '.' + classifier): None,
            ('smsMalwareBenign.k20.' + selector + '.' + classifier): None,
            ('otherMalwareBenign.k20.' + selector + '.' + classifier): None,
    }

    for root, _dirs, files in os.walk('../rocData/'):
        if not set_values(result, files, root):
            return None

    return result


def get_roc_values_by_type_selector(malware_type: str, selector: str) -> None or dict:
    result = {
            (malware_type + 'Benign.k5.' + selector + '.SVM'): None,
            (malware_type + 'Benign.k10.' + selector + '.SVM'): None,
            (malware_type + 'Benign.k20.' + selector + '.SVM'): None,

            (malware_type + 'Benign.k5.' + selector + '.RF'): None,
            (malware_type + 'Benign.k10.' + selector + '.RF'): None,
            (malware_type + 'Benign.k20.' + selector + '.RF'): None,

            (malware_type + 'Benign.k5.' + selector + '.NN'): None,
            (malware_type + 'Benign.k10.' + selector + '.NN'): None,
            (malware_type + 'Benign.k20.' + selector + '.NN'): None
    }

    for root, _dirs, files in os.walk('../rocData/'):
        if not set_values(result, files, root):
            return None

    return result


def roc(result_filename: str, result: dict):
    line_width = 2

    plt.figure()
    for key, value in result.items():
        fpr, tpr, roc_auc = value
        plt.plot(fpr, tpr, lw=line_width, label=key)

    plt.plot([0, 1], [0, 1], 'r--')
    plt.xlim([0.0, 1.0])
    plt.ylim([0.0, 1.05])
    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.title('Receiver operating characteristic')
    plt.legend(loc='lower right')
    plt.savefig('Log_ROC')
    plt.savefig(result_filename)


def main():
    selector = 'mutual'
    # malware_type = 'otherMalware'
    # result = get_roc_values_by_type_selector(malware_type, selector)
    classifier = 'SVM'
    result = get_roc_values_by_classifier_selector(selector, classifier)
    if result is None:
        print('Fail')
        return
    roc('rocClassifierSelector/{}_{}.png.'.format(classifier, selector), result)
    print('Success')


main()
