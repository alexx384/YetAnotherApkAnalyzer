import numpy as np
from sklearn import neural_network
from sklearn import base
from sklearn import feature_selection
from sklearn import model_selection
from sklearn import metrics
from sklearn import preprocessing


DATASET_CSV = 'newResult.csv'
ANSWERS_CSV = 'newAnswer.csv'
LOG_FILENAME = 'log.txt'


def print_reduce(reduce_name: str, accuracy: int, precision: int, recall: int, f1_score: int):
    print('=== ' + reduce_name + ' ===')
    print('Accuracy:  ' + str(accuracy))
    print('Precision: ' + str(precision))
    print('Recall:    ' + str(recall))
    print('F1_score:  ' + str(f1_score))


def cross_validation(dataset: np.ndarray, answers: np.ndarray, model: base.ClassifierMixin,
                     cross_validator: model_selection.BaseCrossValidator, verbose: bool = True,
                     cv_name: str = 'CrossValidator') -> dict:
    scoring = {
        'accuracy': metrics.make_scorer(metrics.accuracy_score),
        'precision': metrics.make_scorer(metrics.precision_score),
        'recall': metrics.make_scorer(metrics.recall_score),
        'f1_score': metrics.make_scorer(metrics.f1_score)
    }
    results = model_selection.cross_validate(estimator=model, X=dataset, y=answers, cv=cross_validator,
                                             scoring=scoring)
    if verbose:
        print_reduce(reduce_name=cv_name,
                     accuracy=results['test_accuracy'],
                     precision=results['test_precision'],
                     recall=results['test_recall'],
                     f1_score=results['test_f1_score'])

    return results


def kfold_cv(block_num: int, dataset: np.ndarray, answers: np.ndarray, model: base.ClassifierMixin,
             verbose: bool = True) -> int:
    cv_name = 'K Fold Validation'
    k_fold = model_selection.KFold(n_splits=block_num, random_state=None, shuffle=False)
    result = cross_validation(dataset, answers, model, k_fold, verbose, cv_name)
    return np.amin(result['test_f1_score'])


def get_score(dataset: np.array, answers: np.array, parametrs: int, model: base.ClassifierMixin, score_func) \
        -> (int, int):
    selecter = feature_selection.SelectKBest(score_func=score_func, k=parametrs)
    selecter.fit(dataset, answers)
    transformed_dataset = selecter.transform(dataset)
    x_train, x_test, y_train, y_test = model_selection.train_test_split(
        transformed_dataset, answers, test_size=0.25, random_state=0)

    model.fit(x_train, y_train)
    prediction = model.predict(x_test)
    simple_score = metrics.f1_score(y_test, prediction, average='binary')

    buffer_test = preprocessing.minmax_scale(dataset, feature_range=(0, 1), axis=0)
    nptraining = np.array(buffer_test, 'float32')
    nptarget = np.array(answers, 'float32')
    k5_score = kfold_cv(5, nptraining, nptarget, model, True)
    return simple_score, k5_score


def save_best_score_report(start_properties_num: int, end_properties_num: int, dataset: np.array, answers: np.array,
                           model: base.ClassifierMixin, log_filename: str, score_func):
    best_sample_score = 0.0
    best_k5_score = 0.0
    best_sample_properties = start_properties_num
    best_k5_properties = start_properties_num

    with open(log_filename, 'w') as file:
        for i in range(start_properties_num, end_properties_num + 1):
            file.write('properties num: ' + str(i))
            sample_score, k5_score = get_score(
                dataset, answers, i, model, score_func)
            file.write('sample_score: ' + str(sample_score) + '\n')
            file.write('k5_score: ' + str(k5_score) + '\n')
            file.flush()

            if best_sample_score < sample_score:
                best_sample_score = sample_score
                best_sample_properties = i
            if best_k5_score < k5_score:
                best_k5_score = k5_score
                best_k5_properties = i

        file.write('The best values')
        file.write('sample_score: ' + str(best_sample_score) + ', properties = ' + str(best_sample_properties) + '\n')
        file.write('k5_score: ' + str(best_k5_score) + ', properties = ' + str(best_k5_properties) + '\n')


def main():
    dataset = np.genfromtxt(DATASET_CSV, dtype=np.int32, delimiter=',')
    answers = np.genfromtxt(ANSWERS_CSV, dtype=np.int32, delimiter=',')
    model = neural_network.MLPClassifier(hidden_layer_sizes=(140, 90, 60, 40), random_state=0)
    save_best_score_report(30, 31, dataset, answers, model, LOG_FILENAME, feature_selection.chi2)


main()
